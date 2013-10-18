/**
 *  TileTypeManager.java
 *  Created on Oct 4, 2013 2:32:05 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogueschema.maps.data.TileMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;

/**
 * Just a simple thing to keep track of which tiles are in memory yet.
 */
public class TileManager {
	
	protected Map<TileMDO, Tile> loadedTiles;
	protected Map<TileMDO, Tile> unloadedTiles;
	
	/**
	 * Creates a new blank tile type manager.
	 */
	public TileManager() {
		this.loadedTiles = new HashMap<TileMDO, Tile>();
		this.unloadedTiles = new HashMap<TileMDO, Tile>();
	}
	
	/**
	 * Make sure the asset manager is prepared to handle a tile MDO. Enqueues
	 * the tile if necessary.
	 * @param	manager			The manager to process from
	 * @param	tileMDO			The data of the tile to load
	 * @param	type			The type of tile to create
	 */
	public void requestTile(AssetManager manager, TileMDO tileMDO, TileType type) {
		if (!loadedTiles.containsKey(tileMDO) && !unloadedTiles.containsKey(tileMDO)) {
			Tile tile = new Tile(tileMDO, type);
			tile.queueRequiredAssets(manager);
			unloadedTiles.put(tileMDO, tile);
		}
	}
	
	/**
	 * Finishes processing any new tiles.
	 * @param	manager			The asset manager to process from
	 * @param	pass			Which pass of the algo we're on, usually 0
	 */
	public void postProcessing(AssetManager manager, int pass) {
		for (Tile tile : unloadedTiles.values()) {
			tile.postProcessing(manager, pass);
			loadedTiles.put(tile.getMDO(), tile);
		}
		unloadedTiles.clear();
	}
	
	/**
	 * Retrieves the required tile, or creates it if not available.
	 * @param	tileMDO			The tile data that tile type is needed for
	 * @param	type			The type of tile we're looking for
	 * @return					The requested tile
	 */
	public Tile getTile(TileMDO tileMDO, TileType type) {
		if (loadedTiles.containsKey(tileMDO)) {
			return loadedTiles.get(tileMDO);
		} else if (unloadedTiles.containsKey(tileMDO)) {
			return unloadedTiles.get(tileMDO);
		} else {
			// I don't like accessing the global here, it's janky
			requestTile(MGlobal.assetManager, tileMDO, type);
			return getTile(tileMDO, type);
		}
		
	}
	
	/**
	 * Retrieves the required tile. Warns if not available.
	 * @param	tileMDO			The tile data that tile is needed for
	 * @return					The requested tile
	 */
	public Tile getTile(TileMDO tileMDO) {
		if (loadedTiles.containsKey(tileMDO)) {
			return loadedTiles.get(tileMDO);
		} else if (unloadedTiles.containsKey(tileMDO)) {
			return unloadedTiles.get(tileMDO);
		} else {
			MGlobal.reporter.warn("No tile found for: " + tileMDO);
			return null;
		}
	}
	
//	/**
//	 * Retrieves the required tile with data specified by MDO with given key, or
//	 * creates it if not available.
//	 * @param	mdoKey			The key of the data file to load from
//	 * @return					The requested tile
//	 */
//	public TileType getTile(String mdoKey) {
//		TileMDO mdo = MGlobal.data.getEntryFor(mdoKey, TileMDO.class);
//		return getTile(mdo);
//	}

}
