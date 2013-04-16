/**
 *  EventFactory.java
 *  Created on Jan 24, 2013 8:46:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.maps.MapObject;

import net.wombatrpgs.rainfall.characters.CharacterFactory;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.custom.CustomEvent;
import net.wombatrpgs.rainfall.maps.custom.EventButton;
import net.wombatrpgs.rainfall.maps.custom.EventChest;
import net.wombatrpgs.rainfall.maps.custom.EventDoorcover;
import net.wombatrpgs.rainfall.maps.custom.EventPressurePad;
import net.wombatrpgs.rainfall.maps.custom.EventTrickWall;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;

/**
 * Creates and handles events from MDOs.
 */
public class EventFactory {
	
	/* these event types are defined in objecttypes.xml */
	protected final static String TYPE_CHARACTER = "event";
	protected final static String TYPE_TELEPORT = "teleport";
	protected final static String TYPE_TELEPORT_Z = "z-teleport";
	protected final static String TYPE_TRIGGER = "trigger";
	protected final static String TYPE_CUSTOM = "object";
	protected final static String TYPE_TARGET = "point";
	
	protected final static String PROPERTY_ID = "id";
	protected final static String PROPERTY_TYPE = "type";
	protected final static String PROPERTY_KEY = "key";
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 * @param 	layerIndex		The index of the layer of the source event
	 * @param	denyPermanents	If true, will not generate permanent events
	 */
	public static void handleData(Level parent, MapObject object, int layerIndex) {
		if (TYPE_TELEPORT_Z.equals(extractType(object))) {
			parent.addEvent(create(parent, object, layerIndex), layerIndex);
			parent.addEvent(create(parent, object, layerIndex), layerIndex+1);
		} else {
			MapEvent event = create(parent, object, layerIndex);
			parent.addEvent(event, layerIndex);
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
	protected static MapEvent create(Level parent, MapObject object, int layerIndex) {
		MapEvent newEvent = null;
		String type = extractType(object);
		if (TYPE_CUSTOM.equals(type)) {
			return createCustom(parent, object, layerIndex);
		} else if (TYPE_CHARACTER.equals(type)) {
			String mdoName = object.getProperties().get(PROPERTY_KEY).toString();
			CharacterEventMDO eventMDO = RGlobal.data.getEntryFor(mdoName, CharacterEventMDO.class);
			newEvent = CharacterFactory.create(eventMDO, object, parent);
		} else if (TYPE_TARGET.equals(type)) {
			newEvent = new TargetPoint(parent, object);
		} else if (TYPE_TELEPORT.equals(type)) {
			newEvent = new TeleportEvent(parent, object);
		} else if (TYPE_TELEPORT_Z.equals(type)) {
			newEvent = new ZTeleportEvent(parent, object, layerIndex);
		} else if (TYPE_TRIGGER.equals(type)){
			newEvent = new Trigger(parent, object);
		} else {
			RGlobal.reporter.warn("Found an event with no type?");
			newEvent = null;
		}
		return newEvent;
	}
	
	/**
	 * Uh oh, we've got a custom object on our hands! This should be implemented
	 * on a per-game basis, really.
	 * @param 	parent			The parent map to create in
	 * @param 	object			The tile object to create from
	 * @param 	z				The z-depth to create in
	 * @return					The customized event
	 */
	protected static CustomEvent createCustom(Level parent, MapObject object, int z) {
		String id = object.getProperties().get(PROPERTY_ID).toString();
		if (id == null) {
			RGlobal.reporter.warn("No ID on custom tiled object: " + object);
			return null;
		}
		id = CustomEvent.EVENT_PREFIX + id;
		if (id.equals(EventPressurePad.ID)) {
			return new EventPressurePad(object, parent);
		} else if (id.equals(EventDoorcover.ID)) {
			return new EventDoorcover(object, parent);
		} else if (id.equals(EventChest.ID)) {
			return new EventChest(object, parent);
		} else if (id.equals(EventButton.ID)) {
			return new EventButton(object, parent);
		} else if (id.equals(EventTrickWall.ID)) {
			return new EventTrickWall(object, parent);
		} else {
			RGlobal.reporter.warn("Unrecognized ID on custom event: " + id + "(" + object + ")");
			return null;
		}
	}
	
	/**
	 * Retrieves the type from a map object via its properties.
	 * @param 	object			The object to extract from
	 * @return					The object's type
	 */
	protected static String extractType(MapObject object) {
		return object.getProperties().get(PROPERTY_TYPE).toString();
	}
	
}
