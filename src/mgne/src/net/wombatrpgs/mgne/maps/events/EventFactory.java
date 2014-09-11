/**
 *  EventFactory.java
 *  Created on Jan 24, 2013 8:46:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.events;

import com.badlogic.gdx.maps.tiled.TiledMap;

import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * Creates and handles events from map objects.
 */
public class EventFactory {
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 */
	public final void createAndPlace(TiledMapObject object) {
		LoadedLevel parent = object.getLevel();
		MapEvent event = createEvent(object);
		parent.addEvent(event, object.getTileX(), object.getTileY());
	}
	
	/**
	 * Creates an event implied from a map property. It's fine to return null
	 * if the property isn't recognized (and that's the default behavior).
	 * @param	map				The tiled map being loaded
	 * @param	key				The key of the map property
	 * @param	value			The string of the map property
	 */
	public MapEvent createFromMapProperty(TiledMap map, String key, String value) {
		return null;
	}
	
	/**
	 * Creates an event from a map object. This can be overridden by specific
	 * games to interpret map objects differently.
	 * @param	object			The tiled object to create events from
	 * @return					The created map event
	 */
	protected MapEvent createEvent(TiledMapObject object) {
		return new MapEvent(object.generateMDO(EventMDO.class));
	}
	
}
