/**
 *  EventFactory.java
 *  Created on Jan 24, 2013 8:46:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps.events;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.maps.LoadedLevel;
import net.wombatrpgs.saga.maps.NpcEvent;
import net.wombatrpgs.saga.maps.TiledMapObject;
import net.wombatrpgs.sagaschema.characters.NpcMDO;

/**
 * Creates and handles events from MDOs.
 */
public class EventFactory {
	
	/** Types of events in a Tiled map, defined in objecttypes.xml */
	protected final static String TYPE_NPC = "npc";
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 */
	public static void createAndPlace(TiledMapObject object) {
		LoadedLevel parent = object.getLevel();
		MapEvent event;
		switch (object.getType()) {
		case NPC:
			event = new NpcEvent(object.generateMDO(NpcMDO.class), parent);
			break;
		default:
			SGlobal.reporter.err("Unsupported event type " + object.getType() +
					" on map " + parent);
			return;
		}
		parent.addEvent(event, object.getTileX(), object.getTileY());
	}
	
}
