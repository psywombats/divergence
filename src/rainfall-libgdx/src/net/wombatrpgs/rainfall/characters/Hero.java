/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.events.CharacterEvent;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent {

	/**
	 * Placeholder constructor
	 * @param parent
	 * @param mdo
	 * @param x
	 * @param y
	 */
	public Hero(Level parent, CharacterEventMDO mdo, int x, int y) {
		super(parent, mdo, x, y);
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

}
