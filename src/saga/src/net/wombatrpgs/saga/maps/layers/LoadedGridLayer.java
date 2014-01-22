/**
 *  LoadedGridLayer.java
 *  Created on Jan 7, 2014 6:20:59 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps.layers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.wombatrpgs.saga.core.Constants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.maps.LoadedLevel;
import net.wombatrpgs.saga.maps.events.MapEvent;

/**
 * A grid layer that was loaded from a Tiled map. Has a couple internal methods
 * for initialization, but apart from that, it should be mostly the same as a
 * normal grid layer.
 */
public class LoadedGridLayer extends GridLayer {
	
	protected LoadedLevel parent;
	protected TiledMapTileLayer layer;

	/**
	 * Creates a new object layer with a parent level and group of objects.
	 * @param 	parent			The parent level of the layer
	 * @param 	layer			The underlying layer of this abstraction
	 */
	public LoadedGridLayer(LoadedLevel parent, TiledMapTileLayer layer) {
		super(parent, Float.valueOf((String) layer.getProperties().get(Constants.PROPERTY_Z)));
		this.parent = parent;
		this.layer = layer;
		if (getProperty(Constants.PROPERTY_Z) == null) {
			SGlobal.reporter.warn("Layer with no Z exists on map " + parent);
		}
	}
	
	/**
	 * @see net.wombatrpgs.saga.maps.layers.Layer#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		OrthogonalTiledMapRenderer renderer = parent.getRenderer();
		renderer.setView(camera);
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer(layer);
		renderer.getSpriteBatch().end();
		
	}

	/**
	 * @see net.wombatrpgs.saga.maps.layers.Layer#isPassable
	 * (net.wombatrpgs.saga.maps.events.MapEvent, int, int)
	 */
	@Override
	public boolean isPassable(MapEvent actor, int x, int y) {
		if (getTileID(x, y) == 0) {
			// there is no tile at this location
			return !isLowerChip();
		} else {
			if (isLowerChip()) {
				return (getTileProperty(x, y, Constants.PROPERTY_IMPASSABLE) == null);
			} else {
				return (getTileProperty(x, y, Constants.PROPERTY_PASSABLE) == null);
			}
			
		}
	}

	/**
	 * An easy way to keep track of properties.
	 * @param 	key				The key of the desired property
	 * @return					The value of that property
	 */
	protected String getProperty(String key) {
		Object val = layer.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}
	
	/**
	 * Extracts the property from a tile at a given cell.
	 * @param 	tileX			The x-coord of the tile to get from (in tiles)
	 * @param 	tileY			The y-coord of the tile to get from (in tiles)
	 * @param 	key				The key of the property to extract
	 * @return					The tile's property at that cell
	 */
	protected String getTileProperty(int tileX, int tileY, String key) {
		Cell cell = layer.getCell(tileX, tileY);
		if (cell == null) return null;
		TiledMapTile tile = cell.getTile();
		Object val = tile.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}
	
	/**
	 * Extracts the tile ID of a given cell.
	 * @param 	tileX			The x-coord of the tile to get from (in tiles)
	 * @param 	tileY			The y-coord of the tile to get from (in tiles)
	 * @return					The tile's ID at that cell
	 */
	protected int getTileID(int tileX, int tileY) {
		Cell cell =  layer.getCell(tileX, tileY);
		return (cell == null) ? 0 : cell.getTile().getId();
	}

}
