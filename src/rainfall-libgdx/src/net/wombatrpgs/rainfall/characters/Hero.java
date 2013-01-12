/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.events.CharacterEvent;
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

	/**
	 * Placeholder constructor. When the herp is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * @param 	parent		The level the hero starts on
	 * @param 	mdo			The character mdo dummy starting the hero
	 * @param 	x			The x-coord (in pixels) to start hero at
	 * @param 	y			The y-coord (in pixels) to start hero at
	 */
	public Hero(Level parent, CharacterEventMDO mdo, int x, int y) {
		super(parent, mdo, x, y);
		moves = new Moveset(RGlobal.data.getEntryFor("default_moveset", MovesetSchema.class));
	}
	
	/**
	 * This default implementation moves us out of collision.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onCollide
	 * (net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void onCollide(MapObject other, CollisionResult result) {
		if (!other.isOverlappingAllowed() && !this.isOverlappingAllowed()) {
			// resolve the collision!!
			// flip if we're not primary
			if (this.getHitbox() == result.collide2) {
				result.mtvX *= -1;
				result.mtvY *= -1;
			}
			this.x += result.mtvX;
			this.y += result.mtvY;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		this.parent.applyPhysicalCorrections(this);
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
		moves.act(command, map);
	}

}
