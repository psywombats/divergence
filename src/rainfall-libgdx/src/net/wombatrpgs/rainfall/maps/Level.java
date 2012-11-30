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
import net.wombatrpgs.rainfall.characters.Hero;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.layers.GridLayer;
import net.wombatrpgs.rainfall.maps.layers.Layer;
import net.wombatrpgs.rainfall.maps.layers.ObjectLayer;
import net.wombatrpgs.rainfallschema.maps.EventMDO;
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
	
	protected static final int TILES_TO_CULL = 8;
	
	protected TileMapRenderer renderer;
	protected TiledMap map;
	protected SpriteBatch batch;
	protected List<Layer> layers; // all object and tile layers in order
	protected List<ObjectLayer> objectLayers;
	protected List<GridLayer> tileLayers;
	protected Map<MapObject, Integer> layerMap; // each object's later
	protected String mapName;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo		Info about the level to generate
	 */
	public Level(MapMDO mdo) {
		mapName = RGlobal.MAPS_DIR + mdo.map;
		batch = new SpriteBatch();
	}
	
	/** @return The batch used to render sprites on this map */
	public SpriteBatch getBatch() { return batch; }
	
	/** @return The width of this map, in pixels */
	public int getWidthPixels() { return map.width * map.tileWidth; }
	
	/** @return The height of this map, in pixels */
	public int getHeightPixels() { return map.height * map.tileHeight; }
	
	/** @return The class used to render this level */
	public TileMapRenderer getRenderer() { return renderer; }
	
	/** @return The underlying TMX map */
	public TiledMap getMap() { return map; }

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
		objectLayers = new ArrayList<ObjectLayer>();
		tileLayers = new ArrayList<GridLayer>();
		layerMap = new HashMap<MapObject, Integer>();
		
		// each object group represents a new layer
		int layerIndex = 0;
		for (TiledObjectGroup group : map.objectGroups) {
			List<MapObject> objects = new ArrayList<MapObject>();
			
			// load up all ingame objects from the database
			for (TiledObject object : group.objects) {
				String mdoName = object.properties.get("key");
				EventMDO eventMdo = (EventMDO) RGlobal.data.getEntryByKey(mdoName);
				MapEvent newEvent;
				if (eventMdo.key.equals("hero_event")) {
					Hero hero = new Hero(this, eventMdo, object.x, 
							map.height*map.tileHeight-object.y);
					RGlobal.hero = hero;
					newEvent = hero;
				} else {
					newEvent = new MapEvent(this, eventMdo, object.x, 
							map.height*map.tileHeight-object.y);
				}
				layerMap.put(newEvent, layerIndex);
				Global.reporter.inform("Loaded event with key " + eventMdo.key);
				objects.add(newEvent);
			}
			
			objectLayers.add(layerIndex, new ObjectLayer(this, objects));
			layerIndex += 1;
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
				layers.add(objectLayers.get(atObject));
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
	 * involves moving it out of said collisions.
	 * @param event
	 */
	public void applyPhysicalCorrections(MapEvent event) {
		if (!layerMap.containsKey(event)) {
			Global.reporter.warn("Event not in layer index: " + event);
			return;
		}
		int layerIndex = layerMap.get(event);
		ObjectLayer activeLayer = objectLayers.get(layerIndex);
		activeLayer.applyPhysicalCorrections(event);
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if (layer == activeLayer) {
				if (i == 0) {
					Global.reporter.warn("Applying layer collisions to an object on " +
							"the bottom of the level: " + event);
					return;
				}
				for (int j = i-1; j >= 0; j--) {
					layers.get(j).applyPhysicalCorrections(event);
				}
			}
		}
	}

}
