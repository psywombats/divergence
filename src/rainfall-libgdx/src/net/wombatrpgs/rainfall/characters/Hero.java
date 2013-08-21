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

import com.badlogic.gdx.maps.MapObject;

import net.wombatrpgs.rainfall.characters.moveset.Moveset;
import net.wombatrpgs.rainfall.characters.moveset.MovesetAct;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.io.audio.SoundObject;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.scenes.FinishListener;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.hero.HeroMDO;
import net.wombatrpgs.rainfallschema.characters.hero.MovesetMDO;
import net.wombatrpgs.rainfallschema.io.data.InputButton;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent {
	
	/** How long the hero gets stunned when touching bad guys */
	protected static float STUN_ON_TOUCH = 1.0f;
	
	/** Holds the moves the player has assigned the hero */
	protected Moveset moves;
	/** Whoo, I'm in RM land!! */
	// TODO: for god's sake why are these in hero.java
	protected Map<String, Boolean> switches;
	/** Plays when we bite the bullet */
	protected SoundObject deathSound;
	/** Where we entered the stage */
	protected float entryX, entryY;

	/**
	 * Placeholder constructor. When the hero is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * @param 	mdo				The character mdo dummy starting the hero
	 * @param	object			The tiled obejct that generated the character
	 * @param 	parent			The level the hero starts on
	 */
	public Hero(HeroMDO mdo, MapObject object, Level parent) {
		super(mdo, object, parent);
		moves = new Moveset(this, RGlobal.data.getEntryFor("default_moveset", MovesetMDO.class));
		assets.add(moves);
		RGlobal.hero = this;
		switches = new HashMap<String, Boolean>();
		if (mdoHasProperty(mdo.deathSound)) {
			deathSound = new SoundObject(
					RGlobal.data.getEntryFor(mdo.deathSound, SoundMDO.class));
			assets.add(deathSound);
		}
	}
	
	/** @return The moveset currently in use by the hero */
	public Moveset getMoves() { return this.moves; }

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCharacterCollide(
	 * net.wombatrpgs.rainfall.characters.CharacterEvent, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		switch (other.mdo.touch) {
		case BOUNCE:
			bounce(other, Math.round(this.getMaxVelocity()));
			break;
		case STUN:
			stun(STUN_ON_TOUCH);
			break;
		case STUNBOUNCE:
			stun(STUN_ON_TOUCH);
			bounce(other, Math.round(this.getMaxVelocity()));
			break;
		case INSTADEATH:
			// hoo boy
			die();
			break;
		default:
			break;
		}
		return super.onCharacterCollide(other, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (canAct() && !getLevel().isPaused()) {
			int targetVX = 0;
			int targetVY = 0;
			if (RGlobal.keymap.isButtonDown(InputButton.DOWN)) targetVY -= 1;
			if (RGlobal.keymap.isButtonDown(InputButton.UP)) targetVY += 1;
			if (RGlobal.keymap.isButtonDown(InputButton.LEFT)) targetVX -= 1;
			if (RGlobal.keymap.isButtonDown(InputButton.RIGHT)) targetVX += 1;
			targetVX *= calcAccelerationMaxVelocity();
			targetVY *= calcAccelerationMaxVelocity();
			targetVelocity(targetVX, targetVY);
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
		if (deathSound != null) deathSound.play();
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
				//stun();
			}
		});
		RGlobal.teleport.getPre().run(getLevel());
	}
	
}
