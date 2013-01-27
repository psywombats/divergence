/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.moveset.Moveset;
import net.wombatrpgs.rainfallschema.hero.MovesetSchema;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent {
	
	/** Holds the moves the player has assigned the hero */
	protected Moveset moves;
	/** Whether or not we're doing something special at the moment */
	protected boolean acting;

	/**
	 * Placeholder constructor. When the herp is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * @param 	mdo			The character mdo dummy starting the hero
	 * @param 	parent		The level the hero starts on
	 * @param 	x			The x-coord (in pixels) to start hero at
	 * @param 	y			The y-coord (in pixels) to start hero at
	 */
	public Hero(CharacterEventMDO mdo, Level parent, int x, int y) {
		super(mdo, parent, x, y);
		moves = new Moveset(this, RGlobal.data.getEntryFor("default_moveset", MovesetSchema.class));
		RGlobal.hero = this;
		this.acting = false;
	}
	
	/** @return True if the hero is currently performing an action */
	public boolean isActing() { return this.acting; }
	
	/** @param True if the hero is current;y performing an action */
	public void setActing(boolean acting) { this.acting = acting; }
	
	/**
	 * This default implementation moves us out of collision.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onCollide
	 * (net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapObject other, CollisionResult result) {
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
		System.out.println("I mean business");
		return super.onCharacterCollide(other, result);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#startMove
	 * (net.wombatrpgs.rainfall.maps.Direction)
	 */
	@Override
	public void startMove(Direction dir) {
		if (RGlobal.block != null && RGlobal.block.isMoving()) return;
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
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		super.postProcessing(manager);
		moves.postProcessing(manager);
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
