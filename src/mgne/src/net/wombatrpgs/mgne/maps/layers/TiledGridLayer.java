/**
 *  TiledGridLayer.java
 *  Created on Aug 21, 2014 12:02:38 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.LoadedLevel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Any layer (loaded or generated) based on a Tiled grid layer.
 */
public class TiledGridLayer extends GridLayer {
	
	protected LoadedLevel parent;
	protected transient TiledMapTileLayer layer;

	/**
	 * Creates a new object layer with a parent level and Tiled grid.
	 * @param 	parent			The parent level of the layer
	 * @param 	layer			The underlying layer of this abstraction
	 */
	public TiledGridLayer(LoadedLevel parent, TiledMapTileLayer layer) {
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
		if (z >= 1) return true;
		if (getTileID(tileX, tileY) == 0) {
			// there is no tile at this location
			return z > 0;
		} else {
			return (getTileProperty(tileX, tileY, Constants.PROPERTY_IMPASSABLE) == null);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.GridLayer#isBridge(int, int)
	 */
	@Override
	public boolean isBridge(int tileX, int tileY) {
		if (isLowerChip()) return false;
		if (getTileID(tileX, tileY) == 0) {
			// there is no tile at this location
			return false;
		} else {
			return (getTileProperty(tileX, tileY, Constants.PROPERTY_PASSABLE) != null);
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
		Cell cell = layer.getCell(tileX, tileY);
		return (cell == null) ? 0 : cell.getTile().getId();
	}

}
