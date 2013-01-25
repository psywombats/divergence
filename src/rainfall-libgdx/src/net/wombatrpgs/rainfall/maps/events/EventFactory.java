/**
 *  EventFactory.java
 *  Created on Jan 24, 2013 8:46:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import net.wombatrpgs.rainfall.characters.CharacterFactory;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

/**
 * Creates and handles events from MDOs.
 */
public class EventFactory {
	
	/* these event types are defined in objecttypes.xml */
	protected final static String EVENT_TYPE = "event";
	protected final static String TELEPORT_TYPE = "teleport";
	protected final static String Z_TELEPORT_TYPE = "z-teleport";
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 * @param 	layerIndex		The index of the layer of the source event
	 */
	public static void handleData(Level parent, TiledObject object, int layerIndex) {
		if (EVENT_TYPE.equals(object.type) ||
			TELEPORT_TYPE.equals(object.type)) {
			parent.addEvent(create(parent, object, layerIndex), layerIndex);
		} else if (Z_TELEPORT_TYPE.equals(object.type)) {
			parent.addEvent(create(parent, object, layerIndex), layerIndex);
			parent.addEvent(create(parent, object, layerIndex), layerIndex+1);
		} else {
			RGlobal.reporter.warn("Was an event with no type: " + object.name);
		}
	}
	
	/**
	 * Creates a map object from tiled object data. Returns the appropriate
	 * subclass of MapEvent. Does not add the map event and is meant to be
	 * called internally.
	 * though.
	 * @param	parent			The level to craft the object for
	 * @param 	object			The tiled object data
	 * @param	layerIndex		The layer index that this object is intended for
	 * @return					Newly minted map object
	 */
	protected static MapEvent create(Level parent, TiledObject object, int layerIndex) {
		if (EVENT_TYPE.equals(object.type)) {
			TiledMap map = parent.getMap();
			String mdoName = object.properties.get("key");
			CharacterEventMDO eventMDO = (CharacterEventMDO) RGlobal.data.getEntryByKey(mdoName);
			return CharacterFactory.create(eventMDO, parent,  
					object.x, 
					map.height*map.tileHeight-object.y);
		} else if (TELEPORT_TYPE.equals(object.type)) {
			return new TeleportEvent(parent, object);
		} else if (Z_TELEPORT_TYPE.equals(object.type)) {
			return new ZTeleportEvent(parent, object, layerIndex);
		} else {
			RGlobal.reporter.warn("Found an event with no type: " + object.name);
			return null;
		}
	}
}
