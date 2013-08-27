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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.graphics.effects.Effect;
import net.wombatrpgs.rainfall.graphics.effects.EffectFactory;
import net.wombatrpgs.rainfall.io.audio.MusicObject;
import net.wombatrpgs.rainfall.maps.custom.CustomEvent;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.layers.GridLayer;
import net.wombatrpgs.rainfall.maps.layers.Layer;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.screen.Screen;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
import net.wombatrpgs.rainfallschema.audio.MusicMDO;
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
	
	/** Max number of tiles an event can have in height, in tiles */
	public static final int MAX_EVENT_HEIGHT = 2;
	
	protected static final String PROPERTY_WIDTH = "width";
	protected static final String PROPERTY_HEIGHT = "height";
	protected static final String PROPERTY_BGM = "bgm";
	
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	
	/** The data this level was created from */
	protected MapMDO mdo;
	/** The thing we're going to use to render the level */
	protected OrthogonalTiledMapRenderer renderer;
	/** The underlying map for this level */
	protected TiledMap map;
	/** The screen we render to */
	protected Screen screen;

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
	protected List<MapThing> objects;
	/** All objects that have registered a unique-per-map id */
	protected Map<String, CustomEvent> customObjects;
	
	/** List of all map events to remove next loop */
	protected List<MapEvent> removalEvents;
	/** List of all map objects to remove next loop */
	protected List<MapThing> removalObjects;
	
	/** Our minimap graphic */
	protected Graphic minimap;
	/** herp derp I wonder what this is */
	protected MusicObject bgm;
	/** An effect that plays on this map, or null */
	protected Effect effect;
	
	/** Should game state be suspended */
	protected boolean paused;
	/** Is the level in an update cycle in which there was a reset */
	protected boolean reseting;
	/** Are we in the process of updating ? */
	protected boolean updating;
	/** Name of the file with our map in it, mentioned in database */
	protected String mapPath;
	
	private List<Queueable> assets;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo				The data to make level from
	 * @param	screen			The screen we render to
	 */
	public Level(MapMDO mdo, Screen screen) {
		this.screen = screen;
		this.mdo = mdo;
		
		mapPath = Constants.MAPS_DIR + mdo.map;
		
		// list init
		layers = new ArrayList<Layer>();
		eventLayers = new ArrayList<EventLayer>();
		tileLayers = new ArrayList<GridLayer>();
		layerMap = new HashMap<MapEvent, Integer>();
		events = new ArrayList<MapEvent>();
		objects = new ArrayList<MapThing>();
		customObjects = new HashMap<String, CustomEvent>();
		removalObjects = new ArrayList<MapThing>();
		removalEvents = new ArrayList<MapEvent>();
		assets = new ArrayList<Queueable>();
		
		if (MapThing.mdoHasProperty(mdo.effect)) {
			effect = EffectFactory.create(this, mdo.effect);
		}
		
		paused = false;
		reseting = false;
	}
	
	/** @return The batch used to render sprites on this map */
	public SpriteBatch getBatch() { return RGlobal.screens.peek().getViewBatch(); }
	
	/** @return The width of this map, in pixels */
	public int getWidthPixels() { return getWidth() * getTileWidth(); }
	
	/** @return The height of this map, in pixels */
	public int getHeightPixels() { return getHeight() * getTileHeight(); }
	
	/** @return The width of this map, in tiles */
	public int getWidth() { return (Integer) map.getProperties().get(PROPERTY_WIDTH); }
	
	/** @return The height of this map, in tiles */
	public int getHeight() { return (Integer) map.getProperties().get(PROPERTY_HEIGHT); }
	
	/** @return The width of each tile on this map, in pixels */
	public int getTileWidth() { return TILE_WIDTH; }
	
	/** @return The height of each tile on this map, in pixels */
	public int getTileHeight() { return TILE_HEIGHT; }
	
	/** @return The class used to render this level */
	public OrthogonalTiledMapRenderer getRenderer() { return renderer; }
	
	/** @return The underlying TMX map */
	public TiledMap getMap() { return map; }
	
	/** @param key The key to index into map properties @return The value */
	public String getProperty(String key) { 
		Object val = map.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}
	
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
	
	/** @return The default bgm for this level */
	public MusicObject getBGM() { return this.bgm; }
	
	/** @param The new BGM object on this level */
	public void setBGM(MusicObject bgm) { this.bgm = bgm; }

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render(
	 * com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		for (float z = 0; z < layers.size() + MAX_EVENT_HEIGHT; z += .5) {
			for (Layer layer : layers) {
				layer.render(camera, z);
			}
		}
		if (effect != null) {
			effect.render(camera);
		}
	}
	
	/**
	 * Queues up all the assets required to render this level in the resource
	 * manager. Does not actually load them. The level should be initialized
	 * first, but this should happen in the constructor.
	 */
	public void queueRequiredAssets(AssetManager manager) {
		RGlobal.reporter.inform("Loading map " + mapPath);
		manager.load(mapPath, TiledMap.class);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (pass == 0) {
			map = RGlobal.assetManager.get(mapPath, TiledMap.class);
			renderer = new OrthogonalTiledMapRenderer(map, 1f);
			renderer.getSpriteBatch().setShader(screen.getMapShader());
			int index = 0;
			for (MapLayer layer : map.getLayers()) {
				// screw you libgdx this casting should /not/ be standard
				Layer created;
				if (TiledMapTileLayer.class.isAssignableFrom(layer.getClass())) {
					GridLayer grid = new GridLayer(this, (TiledMapTileLayer) layer);
					tileLayers.add(grid);
					created = grid;
				} else {
					EventLayer events = new EventLayer(this, layer, index);
					index += 1;
					eventLayers.add(events);
					created = events;
				}
				layers.add(created);
			}
			for (Layer layer : layers) {
				layer.finalizePassability();
			}
			if (getProperty(PROPERTY_BGM) != null) {
				String key = getProperty(PROPERTY_BGM);
				bgm = new MusicObject(RGlobal.data.getEntryFor(key, MusicMDO.class));
				bgm.queueRequiredAssets(manager);
				assets.add(bgm);
			}
			if (effect != null) {
				effect.queueRequiredAssets(manager);
				assets.add(effect);
			}
			for (EventLayer layer : eventLayers) {
				layer.load();
			}
			for (Layer layer : layers) {
				layer.queueRequiredAssets(manager);
			}
		} else if (pass == 1) {
			for (Layer layer : layers) {
				layer.postProcessing(manager, pass-1);
			}
			for (Queueable asset : assets) {
				asset.postProcessing(manager, pass - 1);
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		updating = true;
		for (MapThing toRemove : removalObjects) {
			toRemove.onRemovedFromMap(this);
			internalRemoveObject(toRemove);
		}
		for (MapEvent toRemove : removalEvents) {
			internalRemoveEvent(toRemove);
		}
		removalObjects.clear();
		removalEvents.clear();
		for (int i = 0; i < objects.size(); i++) {
			MapThing object = objects.get(i);
			if (!paused || object.getPauseLevel() != PauseLevel.SURRENDERS_EASILY) {
				object.update(elapsed);
				if (reseting) break;
			}
			object.vitalUpdate(elapsed);
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
		if (effect != null) {
			effect.update(elapsed);
		}
		reseting = false;
		updating = false;
	}
	
	/** @see net.wombatrpgs.rainfall.screen.ScreenShowable#ignoresTint() */
	@Override public boolean ignoresTint() { return false; }

	/** @see java.lang.Object#toString() */
	@Override
	public String toString() { return mapPath + " "; }

	/**
	 * Adjusts an event on the level based on its collisions. This usually
	 * involves moving it out of said collisions. This only works on terrain
	 * and is applying automatically to mobile events in the level.
	 * @param 	event			The mobile event being pushed around
	 */
	public void applyPhysicalCorrections(MapEvent event) {
		if (event == null) {
			RGlobal.reporter.warn("Correcting a null event?");
		}
		Integer layerIndex = layerMap.get(event);
		if (layerIndex == null) {
			RGlobal.reporter.warn("Weird block z-bug...");
			return;
		}
		int activeZ = (int) Math.floor(eventLayers.get(layerIndex).getZ());
		for (int i = 0; i < tileLayers.size(); i++) {
			if (activeZ == Math.floor(tileLayers.get(i).getZ())) {
				tileLayers.get(i).applyPhysicalCorrections(event);
			}
		}
	}
	
	/**
	 * Checks if a certain tile is passable by our stored layer data. This does
	 * not check events at the momement.
	 * @param	actor			The character that will be trying to pass
	 * @param 	tileX			The checked x-coord (in tiles)
	 * @param 	tileY			The checked y-coord (in tiles)
	 * @param	z				The z-depth to restrict the search to (in depth)
	 * @return 					True if layer is passable, false otherwise
	 */
	public boolean isPassable(MapEvent actor, int tileX, int tileY, int z) {
		for (Layer layer : layers) {
			if (Math.floor(layer.getZ()) == z && !layer.isPassable(actor, tileX, tileY)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Performs all collision detection between events, including collision
	 * response and other things that happen when they collide
	 * @param 	event			The event starring in the collisions
	 */
	public void detectCollisions(MapEvent event) {
		Integer layerIndex = layerMap.get(event);
		if (layerIndex == null) {
			RGlobal.reporter.warn("Weird block z-bug (2)...");
			return;
		}
		Integer testZ = (int) Math.floor(eventLayers.get(layerIndex).getZ());
		int activeZ = testZ;
		for (int i = 0; i < eventLayers.size(); i++) {
			if (activeZ == Math.floor(eventLayers.get(i).getZ())) {
				eventLayers.get(i).detectCollisions(event);
			}
		}
	}
	
	/**
	 * Removes an event from this map. The object is assumed not to be the hero.
	 * Control remains on this map.
	 * @param 	toRemove		The map event to remove
	 */
	public void removeEvent(MapEvent toRemove) {
		removalEvents.add(toRemove);
	}
	
	/**
	 * Internall removes an object from all lists and registries. This should
	 * not be used for events, at least not as a primary call.
	 * @param 	toRemove		The event to remove
	 */
	public void removeObject(MapThing toRemove) {
		removalObjects.add(toRemove);
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
		addEventAbsolute(newEvent, tileX * getTileWidth(), tileY * getTileHeight(), z);
	}
	
	/**
	 * Another clone for adding events. This one used float coords for pixels
	 * instead of the usual tile coordinates.
	 * @param 	newEvent		The event to add
	 * @param 	x				The x-coord of the object (in px)
	 * @param 	y				The y-coord of the object (in px)
	 * @param 	z				The z-depth of the object (in ordinal)
	 */
	public void addEventAbsolute(MapEvent newEvent, float x, float y, int z) {
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
	 * @param 	event		The object to change z
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
	public int getZ(MapThing object) {
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
	public void addObject(MapThing object) {
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
		for (MapThing object : objects) {
			object.reset();
		}
	}
	
	/**
	 * Called when hero goes somewhere else or map otherwise ceases to be.
	 */
	public void onFocusLost() {
		for (MapThing object : objects) {
			object.onMapFocusLost(this);
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
	public boolean contains(MapThing object) {
		for (MapEvent victim : removalEvents) {
			if (object == victim) return false;
		}
		for (MapThing victim : removalObjects) {
			if (object == victim) return false;
		}
		for (MapThing other : objects) {
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
	protected void internalRemoveObject(MapThing toRemove) {
		toRemove.onRemovedFromMap(this);
		objects.remove(toRemove);
	}

}
