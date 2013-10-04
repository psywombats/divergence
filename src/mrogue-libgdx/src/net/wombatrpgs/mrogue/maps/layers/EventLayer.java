/**
 *  ObjectLayer.java
 *  Created on Nov 29, 2012 3:46:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.layers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogue.maps.events.MapEvent;

/**
 * A renderable collection of map events, grouped into a layer in a level.
 */
public class EventLayer extends Layer {
	
	protected Level parent;
	protected List<MapEvent> events;
	
	/**
	 * Creates a new object layer with a parent level and no objects.
	 * @param 	parent			The parent level of the layer
	 * @param	layer			The underlying tiled layer
	 * @param	index			The ordinal of this event layer (of event layer)
	 */
	public EventLayer(Level parent) {
		this.parent = parent;
		events = new ArrayList<MapEvent>();
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.layers.Layer#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		parent.getBatch().begin();
		for (MapEvent event : events) {
			event.render(camera);
		}
		parent.getBatch().end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (MapEvent event : events) {
			event.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (MapEvent event : events) {
			event.postProcessing(manager, pass);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		return false;
	}

	/**
	 * Just check if any no-overlap events are in the area.
	 * @see net.wombatrpgs.mrogue.maps.layers.Layer#isPassable
	 * (MapEvent, int, int)
	 */
	@Override
	public boolean isPassable(MapEvent actor, final int x, final int y) {
		// TODO: isPassable
		return true;
	}
	
//	/** @return All events contained on this layer */
	public List<MapEvent> getEvents() { return events; }

	/**
	 * Adds another map event to this layer.
	 * @param 	event			The map event to add
	 */
	public void add(MapEvent event) {
		if (event == null) {
			MGlobal.reporter.warn("Added a null object to the map?");
		} else {
			events.add(event);
			event.onAdd(this);
		}
	}
	
	/**
	 * Removes a map object from this layer.
	 * @param 	event		The map object to remove
	 */
	public void remove(MapEvent event) {
		events.remove(event);
	}
	
	/**
	 * Checks if a map object exists on this layer.
	 * @param 	mapObject		The map object to check if exists
	 * @return					True if the object exists on this layer
	 */
	public boolean contains(MapThing mapObject) {
		return events.contains(mapObject);
	}
	
	/**
	 * Fetches the first event named appropriately. Call from map
	 * @param 	name			The string the event name has to match
	 * @return					The event with matching name, or null if none
	 */
	public MapEvent getEventByName(String name) {
		for (MapEvent event : events) {
			if (name.equals(event.getName())) {
				return event;
			}
		}
		return null;
	}
	
	/**
	 * Returns all events in specified group. Call from map.
	 * @param	groupName		The name of the group to fetch from
	 * @return					A list of all events in that group
	 */
	public List<MapEvent> getEventsByGroup(String groupName) {
		List<MapEvent> result = new ArrayList<MapEvent>();
		for (MapEvent event : events) {
			if (event.inGroup(groupName)) result.add(event);
		}
		return result;
	}

}
