/**
 *  TargetEvent.java
 *  Created on Mar 6, 2013 3:25:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.maps.MapObject;

import net.wombatrpgs.rainfall.maps.Level;

/**
 * A thing on the map with zero physical presence and is just a way to specify
 * coords. If paths ever find their way into the game, those will probably parse
 * to this.
 */
public class TargetPoint extends MapEvent {
	
	/**
	 * Creates a positions a new event. Immobile and no collision detection.
	 * @param 	parent			The level to hold us
	 * @param 	object			The object that spawned us
	 */
	public TargetPoint(Level parent, MapObject object) {
		super(parent, object, false, false);
	}

}
