/**
 *  EventFactory.java
 *  Created on Jan 24, 2013 8:46:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps.events;

import net.wombatrpgs.saga.maps.LoadedLevel;
import net.wombatrpgs.saga.maps.TiledMapObject;
import net.wombatrpgs.sagaschema.maps.EventMDO;

/**
 * Creates and handles events from MDOs.
 */
public class EventFactory {
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 */
	public static void createAndPlace(TiledMapObject object) {
		LoadedLevel parent = object.getLevel();
		MapEvent event = new MapEvent(object.generateMDO(EventMDO.class));
		parent.addEvent(event, object.getTileX(), object.getTileY());
	}
	
}
