/**
 *  Level.java
 *  Created on Nov 12, 2012 6:08:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;

import net.wombatrpgs.rainfall.RGlobal;
import net.wombatrpgs.rainfall.graphics.Renderable;
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
	protected List<List<MapObject>> objects; // sorted by layer
	protected String mapName;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo		Info about the level to generate
	 */
	public Level(MapMDO mdo) {
		TileMapParameter tileMapParameter = new TileMapParameter(
				RGlobal.MAPS_DIR, TILES_TO_CULL, TILES_TO_CULL);
		mapName = RGlobal.MAPS_DIR + mdo.map;
		RGlobal.reporter.inform("We're trying to load from " + mapName);
		RGlobal.assetManager.load(mapName, TileMapRenderer.class, tileMapParameter);
		
		batch = new SpriteBatch();
	}
	
	/** @return The batch used to render sprites on this map */
	public SpriteBatch getBatch() { return batch; }
	
	/** @return The width of this map, in pixels */
	public int getWidthPixels() { return map.width * map.tileWidth; }
	
	/** @return The height of this map, in pixels */
	public int getHeightPixels() { return map.height * map.tileHeight; }

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render(
	 * com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (RGlobal.assetManager.isLoaded(mapName)) {
		
			// make a new renderer if we need one
			if (renderer == null) {
				renderer = RGlobal.assetManager.get(mapName, TileMapRenderer.class);
				map = renderer.getMap();
				objects = new ArrayList<List<MapObject>>();
				
				// each object group represents a new layer
				for (TiledObjectGroup group : map.objectGroups) {
					List<MapObject> list = new ArrayList<MapObject>();
					objects.add(list);
					
					// load up all ingame objects from the database
					for (TiledObject object : group.objects) {
						String mdoName = object.properties.get("key");
						EventMDO eventMdo = (EventMDO) RGlobal.data.getEntryByKey(mdoName);
						list.add(new Event(this, eventMdo, object.x, object.y));
					}
				}
			}
			// TODO: this can be optimized if it's taking too long... profile it
			int atTile = 0;			// tile layer we're rendering
			int atObject = 0;		// object layer we're rendering
			int target = map.layers.size() + map.objectGroups.size() - 1;
			int rendered = 0;		// total objects rendered so far
			while (rendered < target) {
				TiledLayer candidate = map.layers.get(atTile);
				
				// we're either rendering a tiled layer or an object layer
				if (Integer.valueOf(target-rendered).toString().equals(
						candidate.properties.get("layer"))) {
					renderTiles(camera, atTile);
					atTile++;
				} else {
					renderObjects(camera, atObject);
					atObject++;
				}
				rendered++;
			}
		}
	}
	
	/**
	 * Renders an individual tile layer to the screen.
	 * @param 	camera		The camera used to affect the screen
	 * @param 	layerIndex	The layer to render's offset
	 */
	protected void renderTiles(OrthographicCamera camera, int layerIndex) {
		renderer.render(camera, layerIndex);
	}
	
	/**
	 * Renders an individual object layer to the screen.
	 * @param 	camera		The camera used to draw to the screen
	 * @param 	objectIndex	The layer to render's offset
	 */
	protected void renderObjects(OrthographicCamera camera, int objectIndex) {
		batch.begin();
		for (MapObject object : objects.get(objectIndex)) {
			object.render(camera);
		}
		batch.end();
	}

}
