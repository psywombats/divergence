/**
 *  FallResult.java
 *  Created on Dec 31, 2012 3:43:20 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.collisions;

import net.wombatrpgs.rainfall.maps.MapObject;

/**
 * Struct for the result of dropping an item at a specified x/y on a map. This
 * is useful for both dropping the block and objects falling off the sides of
 * things. It holds the z-coord that the collision occurred on and the result of
 * said collision. Requires the hitbox of the falling object to operate.
 */
public class FallResult {
	
	/** 
	 * True if the object stopped falling on this layer, false if it continued
	 * to fall to layers below this one.
	 */
	public boolean finished;
	
	/**
	 * True if the falling object has a legal place to land on the layer, false
	 * if there's some reason it shouldn't land here. This is irrelevant if the
	 * drop hasn't finished.
	 */
	public boolean cleanLanding;
	
	/**
	 * The object that the falling object collided with, if any. Null otherwise.
	 * This usually only has relevance if there was an unclean landing.
	 */
	public MapObject collidingObject;
	
	/**
	 * The z-depth of where the object finished falling.
	 */
	public int z;
	
	@Override
	public String toString() {
		if (!finished) return "FallResult: not finished " + z;
		if (cleanLanding) return "FallResult: clean landing " + z;
		return "FallResult: Fell onto " + collidingObject + " " + z;
	}

}
