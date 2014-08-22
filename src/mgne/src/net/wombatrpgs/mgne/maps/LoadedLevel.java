/**
 *  LoadedLevel.java
 *  Created on Jan 3, 2014 7:25:18 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps;

import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.layers.EventLayer;
import net.wombatrpgs.mgne.maps.layers.LoadedGridLayer;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;

/**
 * A level that's loaded in from a Tiled map. Here's some older stuff.
 * 
 * A Level is comprised of a .tmx tiled map background and a bunch of events
 * that populate it. This thing is a wrapper around Tiled with a few RPG-
 * specific functions built in, like rendering its layers in order so that the
 * player's sprite can appear say above the ground but below a cloud or other
 * upper chip object.
 */
public class LoadedLevel extends Level {
	
	protected static final String KEY_NAME = "name";
	
	protected transient TiledMap map;
	protected transient OrthogonalTiledMapRenderer renderer;
	protected String mapPath;

	/**
	 * Creates a loaded level for a given data for level and screen. This sets
	 * up a level for loading but still requires something else to load up
	 * assets.
	 * @param	mdo				The data to set up loading for
	 * @param	screen			The screen to make a level for
	 */
	public LoadedLevel(LoadedMapMDO mdo, Screen screen) {
		super(mdo, screen);
		mapPath = Constants.MAPS_DIR + mdo.file;
	}
	
	/** Kryo constructor */
	protected LoadedLevel() { }
	
	/** @return The class used to render this level */
	public OrthogonalTiledMapRenderer getRenderer() { return renderer; }
	
	/** @return All objects on this map, discouraged */
	public List<MapThing> getContents() { return objects; }
	
	/** @return The event layer with all events on this map */
	public EventLayer getEventLayer() { return eventLayer; }

	/** @return The tiled map behind this loaded level */
	public TiledMap getMap() { return map; }
	
	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		super.queueRequiredAssets(manager);
		manager.load(mapPath, TiledMap.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		
		if (pass == 0) {
			processMap(manager);
			MGlobal.assets.requestLoading();
		} else {
			eventLayer.postProcessing(manager, pass-1);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#toString()
	 */
	@Override
	public String toString() {
		String name = getProperty(KEY_NAME);
		return (name == null) ? super.toString() : name;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		return map.getProperties().get(key, String.class);
	}

	/**
	 * Does the post processing for the map. This should involve processing the
	 * map itself, and then setting up all the assets the map requires for
	 * processing.
	 * @param	manager			The asset manager to load from
	 */
	private void processMap(MAssets manager) {
		
		// get the map
		map = manager.get(mapPath, TiledMap.class);
		renderer = new OrthogonalTiledMapRenderer(map, 1f);
		renderer.getSpriteBatch().setShader(screen.constructMapShader());
		mapWidth = map.getProperties().get("width", Integer.class);
		mapHeight = map.getProperties().get("height", Integer.class);
	
		// get the layers
		gridLayers.clear();
		boolean generatedEventLayer = false;
		for (MapLayer layer : map.getLayers()) {
			// screw you libgdx this casting should /not/ be standard
			if (TiledMapTileLayer.class.isAssignableFrom(layer.getClass())) {
				gridLayers.add(new LoadedGridLayer(this, (TiledMapTileLayer) layer));
			} else {
				if (generatedEventLayer) {
					MGlobal.reporter.warn("Multiple event layers on map: " + this);
				} else if (eventLayer == null) {
					generatedEventLayer = true;
					eventLayer = new EventLayer(this);
					for (MapObject object : layer.getObjects()) {
						MGlobal.eventFactory.createAndPlace(new TiledMapObject(this, object));
					}
				}
			}
		}
		eventLayer.queueRequiredAssets(manager);
	}
}
