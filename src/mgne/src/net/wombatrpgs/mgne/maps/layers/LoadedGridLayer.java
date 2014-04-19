/**
 *  LoadedGridLayer.java
 *  Created on Jan 7, 2014 6:20:59 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.LoadedLevel;

/**
 * A grid layer that was loaded from a Tiled map. Has a couple internal methods
 * for initialization, but apart from that, it should be mostly the same as a
 * normal grid layer.
 */
public class LoadedGridLayer extends GridLayer {
	
	protected LoadedLevel parent;
	protected transient TiledMapTileLayer layer;

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
			MGlobal.reporter.warn("Layer with no Z exists on map " + parent);
		}
	}

	/**
	 * Ignores the batch for the most part. Sorry.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		OrthogonalTiledMapRenderer renderer = parent.getRenderer();
		renderer.setView(parent.getScreen().getCamera());
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer(layer);
		renderer.getSpriteBatch().end();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.Layer#isTilePassable(int, int)
	 */
	@Override
	public boolean isTilePassable(int tileX, int tileY) {
		if (getTileID(tileX, tileY) == 0) {
			// there is no tile at this location
			return !isLowerChip();
		} else {
			if (isLowerChip()) {
				return (getTileProperty(tileX, tileY, Constants.PROPERTY_IMPASSABLE) == null);
			} else {
				return (getTileProperty(tileX, tileY, Constants.PROPERTY_PASSABLE) == null);
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
