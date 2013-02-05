/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.moveset.Moveset;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.MovesetMDO;
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
	 * Placeholder constructor. When the herp is finally initialized properly
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

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.events.MapEvent, 
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
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
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
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
			bounce(other);
			stun();
			break;
		default:
			break;
		}
		return super.onCharacterCollide(other, result);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#startMove
	 * (net.wombatrpgs.rainfall.maps.Direction)
	 */
	@Override
	public void startMove(Direction dir) {
		if (activeMoves.size() > 0) return;
		super.startMove(dir);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#stopMove
	 * (net.wombatrpgs.rainfall.maps.Direction)
	 */
	@Override
	public void stopMove(Direction dir) {
		if (RGlobal.block != null && RGlobal.block.isMoving()) return;
		super.stopMove(dir);
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
		zeroCoords();
		setX((int) entryX);
		setY((int) entryY);
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

}
