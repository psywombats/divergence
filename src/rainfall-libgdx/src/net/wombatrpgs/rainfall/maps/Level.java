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

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.FallResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.TargetPosition;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.events.EventFactory;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.layers.GridLayer;
import net.wombatrpgs.rainfall.maps.layers.Layer;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfallschema.maps.MapMDO;

/**
 * A Level is comprised of a .tmx tiled map background and a bunch of events
 * that populate it. I can hear you already, "IT'S CALLED A MAP." No need to 
 * conflict with the data structure. Anyway this thing is a wrapper around Tiled
 * with a few RPG-specific functions built in, like rendering its layers in
 * order so that the player's sprite can appear say above the ground but below a
 * cloud or other upper chip object.
 */
public class Level implements Renderable {
	
	public static final int PIXELS_PER_Y = 48;
	public static final int TILES_TO_CULL = 8;
	
	protected TileMapRenderer renderer;
	protected TiledMap map;
	protected SpriteBatch batch;
	protected String mapName;
	protected List<Layer> layers; // all object and tile layers in order
	protected List<EventLayer> eventLayers;
	protected List<GridLayer> tileLayers;
	protected Map<MapEvent, Integer> layerMap; // each object's later
	protected List<MapEvent> events;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo		Info about the level to generate
	 */
	public Level(MapMDO mdo) {
		mapName = RGlobal.MAPS_DIR + mdo.map;
		batch = new SpriteBatch();
		events = new ArrayList<MapEvent>();
	}
	
	/** @return The batch used to render sprites on this map */
	public SpriteBatch getBatch() { return batch; }
	
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
	
	/** @return All tile layers on this map */
	public List<GridLayer> getGridLayers() { return tileLayers; }
	
	/** @return All object layers on this map */
	public List<EventLayer> getObjectLayers() { return eventLayers; }

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render(
	 * com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (RGlobal.assetManager.isLoaded(mapName)) {
			for (Renderable layer : layers) {
				layer.render(camera);
			}
			for (MapEvent event : events) {
				if (event.isCollisionEnabled()) {
					applyPhysicalCorrections(event);
					detectCollisions(event);
				}
			}
		} else {
			Global.reporter.warn("Map assets not loaded for " + mapName);
		}
	}
	
	/**
	 * Queues up all the assets required to render this level in the resource
	 * manager. Does not actually load them. The level should be initialized
	 * first, but this should happen in the constructor.
	 */
	public void queueRequiredAssets(AssetManager manager) {
		TileMapParameter tileMapParameter = new TileMapParameter(
				RGlobal.MAPS_DIR, TILES_TO_CULL, TILES_TO_CULL);
		RGlobal.reporter.inform("We're trying to load from " + mapName);
		RGlobal.assetManager.load(mapName, TileMapRenderer.class, tileMapParameter);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		renderer = RGlobal.assetManager.get(mapName, TileMapRenderer.class);
		map = renderer.getMap();
		layers = new ArrayList<Layer>();
		eventLayers = new ArrayList<EventLayer>();
		tileLayers = new ArrayList<GridLayer>();
		layerMap = new HashMap<MapEvent, Integer>();
		
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
		
		for (int layerIndex = 0; layerIndex < map.objectGroups.size(); layerIndex++) {
			TiledObjectGroup group = map.objectGroups.get(layerIndex);
			// load up all ingame objects from the database
			for (TiledObject object : group.objects) {
				//addEvent(MapEvent.createEvent(this, object), layerIndex);
				EventFactory.handleData(this, object, layerIndex);
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
	}
	
	/**
	 * Same as the rendering asset queuing, but for a second round of map object
	 * assets.
	 * @param 	manager			The manager to queue the object in
	 */
	public void queueMapObjectAssets(AssetManager manager) {
		for (Renderable layer : layers) {
			layer.queueRequiredAssets(manager);
		}
	}
	
	/**
	 * Finish processing the map object loaded previously.
	 * @param 	manager			The manager the map assets were loaded in
	 */
	public void postProcessingMapObjects(AssetManager manager) {
		for (Renderable layer : layers) {
			layer.postProcessing(manager);
		}
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
	 * Teleports the hero off of this map and makes preparations for hero
	 * control no longer on the map.
	 */
	public void teleportOff() {
		RGlobal.hero.parent = null;
		teleportOff(RGlobal.hero);
	}
	
	/**
	 * Removes an event from this map. The object is assumed not to be the hero.
	 * Control remains on this map.
	 * @param 	toRemove		The map event to remove
	 */
	public void teleportOff(MapEvent toRemove) {
		for (EventLayer layer : eventLayers) {
			if (layer.contains(toRemove)) {
				layer.remove(toRemove);
			}
		}
		layerMap.remove(toRemove);
		events.remove(toRemove);
		toRemove.onTeleOff(this);
	}
	
	/**
	 * Welcome a new arrival to this map! The hero! This is specifically made to
	 * transfer control to this level and plop the hero event down at (x,y)
	 * @param 	tileX			The x-coord to teleport to (in tiles)
	 * @param	tileY			The y-coord to teleport to (in tiles)
	 */
	public void teleportOn(int tileX, int tileY, int z) {
		// TODO: don't forget about z
		teleportOn(RGlobal.hero, tileX, tileY);
		RGlobal.screens.getLevelScreen().setCanvas(this);
	}
	
	/**
	 * Welcomes a new event to this map. Does not transfer level control. Event
	 * is assumed to not be the hero. Z is set to 0.
	 * @param 	newEvent		The event to teleport in
	 * @param 	tileX			The initial x-coord (in tiles) of this object
	 * @param 	tileY			The initial y-coord (in tiles) of this object
	 * @param	z				The z-depth of the object (layer index)
	 */
	public void teleportOn(MapEvent newEvent, int tileX, int tileY, int z) {
		newEvent.setX(tileX * map.tileWidth);
		newEvent.setY(tileY * map.tileHeight);
		newEvent.onTeleOn(this);
		addEvent(newEvent, z);
	}
	
	/**
	 * Welcomes a new event to this map. Does not transfer level control. Event
	 * is assumed to not be the hero. Z is set to 0.
	 * @param 	newEvent		The object to teleport in
	 * @param 	tileX			The initial x-coord (in tiles) of this object
	 * @param 	tileY			The initial y-coord (in tiles) of this object
	 */
	public void teleportOn(MapEvent newEvent, int tileX, int tileY) {
		teleportOn(newEvent, tileX, tileY, 0);
	}
	
	/**
	 * Changes an object's z-coordinate on the map. Z-coordinate is handled by
	 * map layer and must be changed here.
	 * @param 	event			The object to change z
	 * @param 	newZ			The index of the layer to put it on
	 */
	public void changeZ(MapEvent event, int newZ) {
		for (EventLayer layer : eventLayers) {
			if (layer.contains(event)) {
				layer.remove(event);
			}
		}
		eventLayers.get(newZ).add(event);
		layerMap.put(event, newZ);
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
	}

}
