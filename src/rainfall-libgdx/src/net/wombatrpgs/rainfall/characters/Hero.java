/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.characters.moveset.Moveset;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.scenes.FinishListener;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.MovesetMDO;
import net.wombatrpgs.rainfallschema.io.data.InputButton;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent {
	
	/** Holds the moves the player has assigned the hero */
	protected Moveset moves;
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
	 * @param 	x				The x-coord (in pixels) to start hero at
	 * @param 	y				The y-coord (in pixels) to start hero at
	 */
	public Hero(CharacterEventMDO mdo, TiledObject object, Level parent, int x, int y) {
		super(mdo, object, parent, x, y);
		moves = new Moveset(this, RGlobal.data.getEntryFor("default_moveset", MovesetMDO.class));
		RGlobal.hero = this;
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
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		moves.postProcessing(manager, pass);
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
	 * Passes the input command to the moveset for appropriate response. This
	 * handles special moves such as jumping and attacks, not the default
	 * movements. Those should be handled by the screen instead, at least until
	 * something better comes up.
	 * @param 	command		The command to pass to the moveset
	 * @param 	map			The map to perform an action on
	 */
	public void act(InputCommand command, Level map) {
		moves.act(command, map, this);
	}

	/**
	 * Goodbye, cruel world! Actually this just resets the level because death
	 * isn't really death in Blockbound.
	 */
	protected void die() {
		stun();
		RGlobal.teleport.getPre().addListener(new FinishListener() {
			@Override
			public void onFinish(Level map) {
				map.reset();
				zeroCoords();
				setX((int) entryX);
				setY((int) entryY);
				RGlobal.teleport.getPost().run(map);
			}
		});
		RGlobal.teleport.getPre().run(getLevel());
	}
}
