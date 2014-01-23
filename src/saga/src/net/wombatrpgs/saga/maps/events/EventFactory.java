/**
 *  EventFactory.java
 *  Created on Jan 24, 2013 8:46:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps.events;

import net.wombatrpgs.saga.core.Constants;
import net.wombatrpgs.saga.maps.LoadedLevel;

import com.badlogic.gdx.maps.MapObject;

/**
 * Creates and handles events from MDOs.
 */
public class EventFactory {
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 */
	public static void handleData(LoadedLevel parent, MapObject object) {
//		MapEvent newEvent = null;
//		String type = extractType(object);
		// TODO: maps: handle event data in factory
	}
	
	/**
	 * Retrieves the type from a map object via its properties.
	 * @param 	object			The object to extract from
	 * @return					The object's type
	 */
	protected static String extractType(MapObject object) {
		return object.getProperties().get(Constants.PROPERTY_TYPE).toString();
	}
	
}
