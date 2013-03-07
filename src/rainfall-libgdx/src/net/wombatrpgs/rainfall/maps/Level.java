/**
 *  Level.java
 *  Created on Nov 12, 2012 6:08:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.custom.CustomEvent;
import net.wombatrpgs.rainfall.maps.events.EventFactory;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.layers.GridLayer;
import net.wombatrpgs.rainfall.maps.layers.Layer;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.FallResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.TargetPosition;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;
import net.wombatrpgs.rainfallschema.maps.MapMDO;

/**
 * A Level is comprised of a .tmx tiled map background and a bunch of events
 * that populate it. I can hear you already, "IT'S CALLED A MAP." No need to 
 * conflict with the data structure. Anyway this thing is a wrapper around Tiled
 * with a few RPG-specific functions built in, like rendering its layers in
 * order so that the player's sprite can appear say above the ground but below a
 * cloud or other upper chip object.
 */
public class Level implements ScreenShowable {
	
	/** How many pixels are occupied by each point of z-depth */
	public static final int PIXELS_PER_Y = 48; // in pixels
	/** libgdx bullshit */
	public static final int TILES_TO_CULL = 8; // in... I don't know
	/** Max number of tiles an event can have in height, in tiles */
	public static final int MAX_EVENT_HEIGHT = 3;
	
	public static final String PROPERTY_MINIMAP_GRAPHIC = "minimap";
	public static final String PROPERTY_MINIMAP_X1 = "minimap_x1";
	public static final String PROPERTY_MINIMAP_Y1 = "minimap_y1";
	public static final String PROPERTY_MINIMAP_X2 = "minimap_x2";
	public static final String PROPERTY_MINIMAP_Y2 = "minimap_y2";
	
	/** The thing we're going to use to render the level */
	protected TileMapRenderer renderer;
	/** The underlying map for this level */
	protected TiledMap map;

	/** All event and tile layers, in order by Z */
	protected List<Layer> layers;
	/** All event layers, in order by Z */
	protected List<EventLayer> eventLayers;
	/** All tile layers, in order by Z */
	protected List<GridLayer> tileLayers;
	/** A mapping from object to their z-depths */
	protected Map<MapEvent, Integer> layerMap;
	
	/** List of all map events in the level, (these are all in layers) */
	protected List<MapEvent> events;
	/** List of all map object in the level (some are in layers) */
	protected List<MapObject> objects;
	/** All objects that have registered a unique-per-map id */
	protected Map<String, CustomEvent> customObjects;
	
	/** List of all map events to remove next loop */
	protected List<MapEvent> removalEvents;
	/** List of all map objects to remove next loop */
	protected List<MapObject> removalObjects;
	
	/** Our minimap graphic */
	protected Graphic minimap;
	/** Should game state be suspended */
	protected boolean paused;
	/** Is the level in an update cycle in which there was a reset */
	protected boolean reseting;
	/** Are we in the process of updating ? */
	protected boolean updating;
	/** Name of the file with our map in it, mentioned in database */
	protected String mapPath;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo		Info about the level to generate
	 */
	public Level(MapMDO mdo) {
		mapPath = Constants.MAPS_DIR + mdo.map;
		layers = new ArrayList<Layer>();
		eventLayers = new ArrayList<EventLayer>();
		tileLayers = new ArrayList<GridLayer>();
		layerMap = new HashMap<MapEvent, Integer>();
		events = new ArrayList<MapEvent>();
		objects = new ArrayList<MapObject>();
		customObjects = new HashMap<String, CustomEvent>();
		removalObjects = new ArrayList<MapObject>();
		removalEvents = new ArrayList<MapEvent>();
		
		paused = false;
		reseting = false;
	}
	
	/** @return The batch used to render sprites on this map */
	public SpriteBatch getBatch() { return RGlobal.screens.peek().getBatch(); }
	
	/** @return The width of this map, in pixels */
	public int getWidthPixels() { return map.width * map.tileWidth; }
	
	/** @return The height of this map, in pixels */
	public int getHeightPixels() { return map.height * map.tileHeight; }
	
	/** @return The width of this map, in tiles */
	public int getWidth() { return map.width; }
	
	/** @return The height of this map, in tiles */
	public int getHeight() { return map.height; }
	
	/** @return The width of each tile on this map, in pixels */
	public int getTileWidth() { return map.tileWidth; }
	
	/** @return The height of each tile on this map, in pixels */
	public int getTileHeight() { return map.tileHeight; }
	
	/** @return The class used to render this level */
	public TileMapRenderer getRenderer() { return renderer; }
	
	/** @return The underlying TMX map */
	public TiledMap getMap() { return map; }
	
	/** @param key The key to index into map properties @return The value */
	public String getProperty(String key) { return map.properties.get(key); }
	
	/** @return All tile layers on this map */
	public List<GridLayer> getGridLayers() { return tileLayers; }
	
	/** @return All object layers on this map */
	public List<EventLayer> getEventLayers() { return eventLayers; }
	
	/** @return All events on this map */
	public List<MapEvent> getEvents() { return events; }
	
	/** @param pause The map object to pause on */
	public void setPause(boolean paused) { this.paused = paused; }
	
	/** @return True if the level is in a suspended state */
	public boolean isPaused() { return this.paused; }
	
	/** @return The graphic of the minimap to display on the map */
	public Graphic getMinimap() { return this.minimap; }

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render(
	 * com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		for (int z = 0; z < layers.size() + MAX_EVENT_HEIGHT; z++) {
			for (Layer layer : layers) {
				layer.render(camera, z);
			}
		}
	}
	
	/**
	 * Queues up all the assets required to render this level in the resource
	 * manager. Does not actually load them. The level should be initialized
	 * first, but this should happen in the constructor.
	 */
	public void queueRequiredAssets(AssetManager manager) {
		TileMapParameter tileMapParameter = new TileMapParameter(
				Constants.MAPS_DIR, TILES_TO_CULL, TILES_TO_CULL);
		RGlobal.reporter.inform("Loading map " + mapPath);
		manager.load(mapPath, TileMapRenderer.class, tileMapParameter);
		if (minimap != null) {
			minimap.queueRequiredAssets(manager);
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (pass >= 1) {
			for (Layer layer : layers) {
				layer.postProcessing(manager, pass-1);
			}
			if (minimap != null) {
				minimap.postProcessing(manager, pass-1);
			}
			return;
		}
		renderer = RGlobal.assetManager.get(mapPath, TileMapRenderer.class);
		map = renderer.getMap();
		
		// each object group represents a new layer
		for (int layerIndex = 0; layerIndex <= map.objectGroups.size(); layerIndex++) {
			// create a new layer to add things to
			TiledObjectGroup group;
			if (layerIndex < map.objectGroups.size()) {
				group = map.objectGroups.get(layerIndex);
			} else {
				group = new TiledObjectGroup();
				group.properties.put(Layer.PROPERTY_Z, String.valueOf((layerIndex+.5)));
			}
			eventLayers.add(layerIndex, new EventLayer(this, group));
		}
		
		for (int i = 0; i < map.objectGroups.size(); i++) {
			TiledObjectGroup group = map.objectGroups.get(i);
			// load up all ingame objects from the database
			for (TiledObject object : group.objects) {
				//addEvent(MapEvent.createEvent(this, object), layerIndex);
				EventFactory.handleData(this, object, i);
			}
		}
		
		for (int i = 0; i < map.layers.size(); i++) {
			tileLayers.add(i, new GridLayer(this, map.layers.get(i), i));
		}
		
		// sort the layers by their original index
		int atTile = 0;			// tile layer we're adding
		int atObject = 0;		// object layer we're adding
		int target = map.layers.size() + map.objectGroups.size();
		int added = 0;			// total layers added
		while (added < target) {
			if (atTile < map.layers.size() && 
					Integer.valueOf(target-added-1).toString().equals(
					map.layers.get(atTile).properties.get("layer"))) {
				layers.add(tileLayers.get(atTile));
				atTile++;
			} else {
				layers.add(eventLayers.get(atObject));
				atObject++;
			}
			added++;
		}
		
		for (Layer layer : layers) {
			layer.finalizePassability();
		}
		if (getProperty(PROPERTY_MINIMAP_GRAPHIC) != null) {
			String key = getProperty(PROPERTY_MINIMAP_GRAPHIC);
			minimap = new Graphic(RGlobal.data.getEntryFor(key, GraphicMDO.class));
			minimap.queueRequiredAssets(manager);
		}
		for (Layer layer : layers) {
			layer.queueRequiredAssets(manager);
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		updating = true;
		for (MapObject toRemove : removalObjects) {
			internalRemoveObject(toRemove);
		}
		for (MapEvent toRemove : removalEvents) {
			internalRemoveEvent(toRemove);
		}
		removalObjects.clear();
		removalEvents.clear();
		for (int i = 0; i < objects.size(); i++) {
			MapObject object = objects.get(i);
			object.vitalUpdate(elapsed);
			if (!paused || object.getPauseLevel() != PauseLevel.SURRENDERS_EASILY) {
				object.update(elapsed);
				if (reseting) break;
			}
		}
		if (!reseting) {
			for (int i = 0; i < events.size(); i++) {
				MapEvent event = events.get(i);
				if (event.isCollisionEnabled()) {
					applyPhysicalCorrections(event);
					detectCollisions(event);
				}
			}
		}
		reseting = false;
		updating = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.screen.ScreenShowable#ignoresTint()
	 */
	@Override
	public boolean ignoresTint() {
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mapPath + " ";
	}

	/**
	 * Adjusts an event on the level based on its collisions. This usually
	 * involves moving it out of said collisions. This only works on terrain
	 * and is applying automatically to mobile events in the level.
	 * @param 	event			The mobile event being pushed around
	 */
	public void applyPhysicalCorrections(MapEvent event) {
		int layerIndex = layerMap.get(event);
		int activeZ = (int) Math.floor(eventLayers.get(layerIndex).getZ());
		for (int i = 0; i < tileLayers.size(); i++) {
			if (activeZ == Math.floor(tileLayers.get(i).getZ())) {
				tileLayers.get(i).applyPhysicalCorrections(event);
			}
		}
	}
	
	/**
	 * Performs all collision detection between events, including collision
	 * response and other things that happen when they collide
	 * @param 	event			The event starring in the collisions
	 */
	public void detectCollisions(MapEvent event) {
		int layerIndex = layerMap.get(event);
		int activeZ = (int) Math.floor(eventLayers.get(layerIndex).getZ());
		for (int i = 0; i < eventLayers.size(); i++) {
			if (activeZ == Math.floor(eventLayers.get(i).getZ())) {
				eventLayers.get(i).detectCollisions(event);
			}
		}
	}
	
	/**
	 * Drops an object on the implicit location, returning the result of the
	 * drop.
	 * @param 	box			The hitbox of the falling object
	 * @param	start		The starting z of the falling object
	 * @param	target		The target positionable to adjust for z-correction
	 * @return				The result of the fall
	 */
	public FallResult dropObject(Hitbox box, float start, TargetPosition target) {
		int originalY = target.getY();
		int i = layers.size()-1;
		while (layers.get(i).getZ() > start) i -= 1;
		for (; i >= 0; i--) {
			Layer layer = layers.get(i);
			int deltaZ = (int) (Math.floor(start) - Math.floor(layer.getZ()));
			target.setY(originalY - deltaZ * PIXELS_PER_Y);
			FallResult layerResult = layer.dropObject(box);
			if (layerResult.finished) return layerResult;
		}
		FallResult result = new FallResult();
		result.finished = false;
		return result;
	}
	
	/**
	 * Removes an event from this map. The object is assumed not to be the hero.
	 * Control remains on this map.
	 * @param 	toRemove		The map event to remove
	 */
	public void removeEvent(MapEvent toRemove) {
		if (updating) {
			removalEvents.add(toRemove);
		} else {
			internalRemoveEvent(toRemove);
		}
	}
	
	/**
	 * Internall removes an object from all lists and registries. This should
	 * not be used for events, at least not as a primary call.
	 * @param 	toRemove		The event to remove
	 */
	public void removeObject(MapObject toRemove) {
		if (updating) {
			removalObjects.add(toRemove);
		} else {
			internalRemoveObject(toRemove);
		}
	}
	
	/**
	 * Welcome a new arrival to this map! The hero! This is specifically made to
	 * transfer control to this level and plop the hero event down at (x,y)
	 * @param 	tileX			The x-coord to teleport to (in tiles)
	 * @param	tileY			The y-coord to teleport to (in tiles)
	 */
	public void teleportOn(int tileX, int tileY, int z) {
		addEvent(RGlobal.hero, tileX, tileY);
		RGlobal.screens.peek().setCanvas(this);
	}
	
	/**
	 * Welcomes a new event to this map. Does not transfer level control. Event
	 * is assumed to not be the hero. Z is set to 0.
	 * @param 	newEvent		The event to teleport in
	 * @param 	tileX			The initial x-coord (in tiles) of this object
	 * @param 	tileY			The initial y-coord (in tiles) of this object
	 * @param	z				The z-depth of the object (layer index)
	 */
	public void addEvent(MapEvent newEvent, int tileX, int tileY, int z) {
		addEventAbsolute(newEvent, tileX * map.tileWidth, tileY * map.tileHeight, z);
	}
	
	/**
	 * Another clone for adding events. This one used float coords for pixels
	 * instead of the usual tile coordinates.
	 * @param 	newEvent		The event to add
	 * @param 	x				The x-coord of the object (in px)
	 * @param 	y				The y-coord of the object (in px)
	 * @param 	z				The z-depth of the object (in ordinal)
	 */
	public void addEventAbsolute(MapEvent newEvent, int x, int y, int z) {
		newEvent.setX(x);
		newEvent.setY(y);
		addEvent(newEvent, z);
	}
	
	/**
	 * Welcomes a new event to this map. Does not transfer level control. Event
	 * is assumed to not be the hero. Z is set to 0.
	 * @param 	newEvent		The object to teleport in
	 * @param 	tileX			The initial x-coord (in tiles) of this object
	 * @param 	tileY			The initial y-coord (in tiles) of this object
	 */
	public void addEvent(MapEvent newEvent, int tileX, int tileY) {
		addEvent(newEvent, tileX, tileY, 0);
	}
	
	/**
	 * Adds a new event to this map. Called internally for maps in the actual
	 * map resources and externally by events that should've been there but
	 * aren't for convenience reasons.
	 * @param 	newEvent		The new event to add
	 * @param 	layerIndex		The number of the layer to add on
	 */
	public void addEvent(MapEvent newEvent, int layerIndex) {
		layerMap.put(newEvent, layerIndex);
		eventLayers.get(layerIndex).add(newEvent);
		events.add(newEvent);
		addObject(newEvent);
		newEvent.onAddedToMap(this);
	}
	
	/**
	 * Changes an object's z-coordinate on the map. Z-coordinate is handled by
	 * map layer and must be changed here.
	 * @param 	event			The object to change z
	 * @param 	z			The index of the layer to put it on
	 */
	public void changeZ(MapEvent event, float z) {
		for (EventLayer layer : eventLayers) {
			if (layer.contains(event)) {
				layer.remove(event);
			}
		}
		for (EventLayer layer : eventLayers) {
			if (layer.getZ() == z) {
				layer.add(event);
			}
		}
		layerMap.put(event, (int) Math.floor(z));
	}
	
	/**
	 * Gets the z-coord of an object, which is just its index in the object
	 * layer stack.
	 * @param 	object			The object to get the coord of
	 * @return					The z-coord of that object
	 */
	public int getZ(MapObject object) {
		return layerMap.get(object);
	}
	
	/**
	 * Finds and returns the event named apporpriately. Behaves weirdly if
	 * multiple events have the same name.
	 * @param 	name			The name of the event we're looking for
	 * @return					An event named that way, or null if none
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
	 * Finds and returns all events with the given group name. Numerous for
	 * commanding a bunch of events at once. Events can belong to multiple
	 * groups by putting the ';' between their group name.
	 * @param 	groupName		The group name to search for
	 * @return					All events belonging to that group
	 */
	public List<MapEvent> getEventsByGroup(String groupName) {
		List<MapEvent> result = new ArrayList<MapEvent>();
		for (MapEvent event : events) {
			if (event.inGroup(groupName)) result.add(event);
		}
		return result;
	}
	
	/**
	 * Adds a new object to this map. Called externally for anything wanting to
	 * add non-events to this map.
	 * @param 	object			The new object to add
	 */
	public void addObject(MapObject object) {
		if (objects.contains(object)) {
			if (removalEvents.contains(object)) {
				internalRemoveEvent((MapEvent) object);
				RGlobal.reporter.inform("Overlapped remove/add event: " + object);
			} else if (removalObjects.contains(object)) {
				internalRemoveObject(object);
				RGlobal.reporter.inform("Overlapped remove/add object: " + object);
			} else {
				RGlobal.reporter.warn("Added the same object twice: " + object);
			}
		}
		objects.add(object);
		object.onAddedToMap(this);
	}
	
	/**
	 * Adds a passable event hitbox to the level. Same as the gridlayer method.
	 * Note that this can only add to gridlayers with integer z values. This is
	 * somewhat intentional as overriding the upper chip with an upper chip
	 * event is pointless.
	 * @param 	box				The hitbox to add as an override
	 * @param	z				The z of the layer to add it on
	 */
	public void addPassabilityOverride(Hitbox box, int z) {
		for (GridLayer layer : tileLayers) {
			if (layer.getZ() == z) {
				layer.addPassabilityOverride(box);
			}
		}
	}
	
	/**
	 * Removes a passable event hitbox from the level.
	 * @param 	box				The override to remove
	 * @param 	z				The z of the layer to remove it from
	 */
	public void removePassabilityOverride(Hitbox box, int z) {
		for (GridLayer layer : tileLayers) {
			if (layer.getZ() == z) {
				layer.removePassabilityOverride(box);
			}
		}
	}
	
	/**
	 * Resets the level to how it was during its intial load. This should keep
	 * important things like puzzle solved status but remove things like enemy
	 * deaths and reset event positions.
	 */
	public void reset() {
		reseting = true;
		if (contains(RGlobal.block)) {
			removeEvent(RGlobal.block);
		}
		for (MapObject object : objects) {
			object.reset();
		}
	}
	
	/**
	 * Determiens if an object will exist on this level in the upcoming update.
	 * This doesn't check the active objects, but also the objects in the queue
	 * for adding and excludes the removal queue. (actually there is no add
	 * queue right now so uh)
	 * @param 	object			The object to check if exists
	 * @return					True if that object will be on the map
	 */
	public boolean contains(MapObject object) {
		for (MapEvent victim : removalEvents) {
			if (object == victim) return false;
		}
		for (MapObject victim : removalObjects) {
			if (object == victim) return false;
		}
		for (MapObject other : objects) {
			if (object == other) return true;
		}
		return false;
	}
	
	/**
	 * Registers a unique-per-map ID object in this level's database. Does not
	 * actually add the object to the map, just the registry.
	 * @param 	object			The object to add to registry
	 */
	public void registerCustomObject(CustomEvent object) {
		customObjects.put(object.getID(), object);
	}
	
	/**
	 * Get a previously registered unique-per-map ID object from this level's
	 * database. No guarantees on if the object has actually been added yet.
	 * @param 	id				The string ID of the object to fetch
	 * @return					The object from the registry with that key
	 */
	public CustomEvent getCustomObject(String id) {
		return customObjects.get(id);
	}
	
	/**
	 * Internally removes an event from all lists and registries.
	 * @param 	toRemove		The event to remove
	 */
	protected void internalRemoveEvent(MapEvent toRemove) {
		internalRemoveObject(toRemove);
		toRemove.onRemovedFromMap(this);
		for (EventLayer layer : eventLayers) {
			if (layer.contains(toRemove)) {
				layer.remove(toRemove);
			}
		}
		layerMap.remove(toRemove);
		events.remove(toRemove);
	}
	
	/**
	 * Called when this object is removed from the map.
	 * @param 	toRemove		The object to remove
	 */
	protected void internalRemoveObject(MapObject toRemove) {
		toRemove.onRemovedFromMap(this);
		objects.remove(toRemove);
	}

}
