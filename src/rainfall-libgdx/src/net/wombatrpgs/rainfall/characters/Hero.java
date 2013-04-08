/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.characters.moveset.ActSummon;
import net.wombatrpgs.rainfall.characters.moveset.Moveset;
import net.wombatrpgs.rainfall.characters.moveset.MovesetAct;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.io.audio.SoundObject;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.scenes.FinishListener;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.hero.HeroMDO;
import net.wombatrpgs.rainfallschema.characters.hero.MovesetMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.io.data.InputButton;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent {
	
	/** Holds the moves the player has assigned the hero */
	protected Moveset moves;
	/** Whoo, I'm in RM land!! */
	// TODO: for god's sake why are these in hero.java
	protected Map<String, Boolean> switches;
	/** Plays when we bite the bullet */
	protected SoundObject deathSound;
	/** Where we entered the stage */
	protected float entryX, entryY;
	/** How many times enemies have consecutively mugged us */
	protected int stunMug = 0;
	/** Is the hero in the process of eating fire? */
	protected boolean dying;
	
	private static final String KEY_MOVE_SUMMON2 = "move_summon";
	private MovesetAct summon2; // I'm sorry, but I'm hardcoding this. I'm tired.

	/**
	 * Placeholder constructor. When the hero is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * @param 	mdo				The character mdo dummy starting the hero
	 * @param	object			The tiled obejct that generated the character
	 * @param 	parent			The level the hero starts on
	 * @param 	x				The x-coord (in pixels) to start hero at
	 * @param 	y				The y-coord (in pixels) to start hero at
	 */
	public Hero(HeroMDO mdo, TiledObject object, Level parent, int x, int y) {
		super(mdo, object, parent, x, y);
		moves = new Moveset(this, RGlobal.data.getEntryFor("default_moveset", MovesetMDO.class));
		RGlobal.hero = this;
		switches = new HashMap<String, Boolean>();
		summon2 = new ActSummon(this, RGlobal.data.getEntryFor(KEY_MOVE_SUMMON2, SummonMDO.class));
		deathSound = new SoundObject(RGlobal.data.getEntryFor(mdo.deathSound, SoundMDO.class), this);
		dying = false;
	}
	
	/** @return The moveset currently in use by the hero */
	public Moveset getMoves() { return this.moves; }

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.events.MapEvent, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (other == RGlobal.block) {
			float ratio;
			if (RGlobal.block.isMoving()) {
				ratio = 0f;
			} else {
				ratio = 1f;
			}
			applyMTV(other, result, ratio);
			return false;
		} else {
			return super.onCollide(other, result);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCharacterCollide(
	 * net.wombatrpgs.rainfall.characters.CharacterEvent, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		switch (other.mdo.touch) {
		case BOUNCE:
			bounce(other);
			break;
		case STUN:
			stun();
			break;
		case STUNBOUNCE:
			stun();
			bounce(other);
			break;
		case INSTADEATH:
			// hoo boy
			// TODO: set the hero on fire
			die();
			break;
		default:
			break;
		}
		return super.onCharacterCollide(other, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		moves.queueRequiredAssets(manager);
		summon2.queueRequiredAssets(manager);
		deathSound.queueRequiredAssets(manager);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		moves.postProcessing(manager, pass);
		summon2.postProcessing(manager, pass);
		deathSound.postProcessing(manager, pass);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (activeMoves.size() <= 0 && canAct() && !getLevel().isPaused()) {
			int targetVX = 0;
			int targetVY = 0;
			if (RGlobal.keymap.isButtonDown(InputButton.DOWN)) targetVY -= 1;
			if (RGlobal.keymap.isButtonDown(InputButton.UP)) targetVY += 1;
			if (RGlobal.keymap.isButtonDown(InputButton.LEFT)) targetVX -= 1;
			if (RGlobal.keymap.isButtonDown(InputButton.RIGHT)) targetVX += 1;
			targetVX *= maxVelocity;
			targetVY *= maxVelocity;
			targetVelocity(targetVX, targetVY);
		}
		// more hardcoding issues
		if (isSet("intro_done")) {
			moves.getMoves().put(InputCommand.ACTION_1, summon2);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#onAdd
	 * (net.wombatrpgs.rainfall.maps.layers.EventLayer)
	 */
	@Override
	public void onAdd(EventLayer layer) {
		super.onAdd(layer);
		entryX = getX();
		entryY = getY();
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#endFall()
	 */
	@Override
	public void endFall() {
		die();
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#stun()
	 */
	@Override
	public void stun() {
		if (stunned) {
			stunMug += 1;
			if (stunMug == 3) {
				die();
				return;
			}
		} else {
			stunMug = 0;
		}
		super.stun();
	}
	
	/**
	 * Checks to see if a switch is set. Switches are guaranteed to be
	 * persistent across hero, and the primary way of storing event persistence,
	 * even if events are stored...
	 * @param 	swit			The switch to check
	 * @return					True if that switch is set, false otherwise
	 */
	public boolean isSet(String swit) {
		Boolean result = switches.get(swit);
		return (result != null && result);
	}
	
	/**
	 * Sets a switch in memory.
	 * @param 	swit			The name of the switch to set
	 * @param 	value			The value to set it to
	 */
	public void setSwitch(String swit, boolean value) {
		switches.put(swit, value);
	}

	/**
	 * Passes the input command to the moveset for appropriate response. This
	 * handles special moves such as jumping and attacks, not the default
	 * movements. Those should be handled by the screen instead, at least until
	 * something better comes up.
	 * @param 	command		The command to pass to the moveset
	 * @param 	map			The map to perform an action on
	 */
	public void act(InputCommand command, Level map) {
		moves.act(command, map, this);
		appearance.startMoving();
	}
	
	/**
	 * Cancels all actions currently being performed.
	 */
	public void cancelActions() {
		List<MovesetAct> toCancel = new ArrayList<MovesetAct>();
		toCancel.addAll(activeMoves);
		for (MovesetAct act : toCancel) {
			act.cancel();
		}
	}

	/**
	 * Goodbye, cruel world! Actually this just resets the level because death
	 * isn't really death in Blockbound.
	 */
	public void die() {
		if (dying) return;
		dying = true;
		deathSound.play();
		List<MovesetAct> toCancel = new ArrayList<MovesetAct>();
		toCancel.addAll(activeMoves);
		for (MovesetAct act : toCancel) {
			act.cancel();
		}
		activeMoves.clear();
		RGlobal.teleport.getPre().addListener(new FinishListener() {
			@Override
			public void onFinish(Level map) {
				map.reset();
				zeroCoords();
				setX((int) entryX);
				setY((int) entryY);
				RGlobal.teleport.getPost().run(map);
				dying = false;
				//stun();
			}
		});
		RGlobal.teleport.getPre().run(getLevel());
	}
}
