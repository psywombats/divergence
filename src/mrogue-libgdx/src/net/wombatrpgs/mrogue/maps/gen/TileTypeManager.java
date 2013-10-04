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
import net.wombatrpgs.mrogue.maps.TileType;
import net.wombatrpgs.mrogueschema.maps.TileMDO;

/**
 * Just a simple thing to keep track of which tiles are in memory yet.
 */
public class TileTypeManager {
	
	protected Map<TileMDO, TileType> loadedTiles;
	protected Map<TileMDO, TileType> unloadedTiles;
	
	/**
	 * Creates a new blank tile type manager.
	 */
	public TileTypeManager() {
		this.loadedTiles = new HashMap<TileMDO, TileType>();
		this.unloadedTiles = new HashMap<TileMDO, TileType>();
	}
	
	/**
	 * Make sure the asset manager is prepared to handle a tile MDO. Enqueues
	 * the tile if necessary.
	 * @param	manager			The manager to process from
	 * @param	tileMDO			The data of the tile to load
	 */
	public void requestTile(AssetManager manager, TileMDO tileMDO) {
		if (!loadedTiles.containsKey(tileMDO) && !unloadedTiles.containsKey(tileMDO)) {
			TileType tile = new TileType(tileMDO);
			tile.queueRequiredAssets(manager);
			unloadedTiles.put(tileMDO, tile);
		}
	}
	
	/**
	 * Make sure the MDO indicated by the name is prepated to handle a tile.
	 * @param	manager			The manager to process from
	 * @param	mdoKey			The name of the MDO file with data to load
	 */
	public void requestTile(AssetManager manager, String mdoKey) {
		TileMDO mdo = MGlobal.data.getEntryFor(mdoKey, TileMDO.class);
		requestTile(manager, mdo);
	}
	
	/**
	 * Finishes processing any new tiles.
	 * @param	manager			The asset manager to process from
	 * @param	pass			Which pass of the algo we're on, usually 0
	 */
	public void postProcessing(AssetManager manager, int pass) {
		for (TileType tile : unloadedTiles.values()) {
			tile.postProcessing(manager, pass);
			loadedTiles.put(tile.getMDO(), tile);
		}
		unloadedTiles.clear();
	}
	
	/**
	 * Retrieves the required tile, or creates it if not available.
	 * @param	tileMDO			The tile data that tile type is needed for
	 * @return					The requested tile
	 */
	public TileType getTile(TileMDO tileMDO) {
		if (loadedTiles.containsKey(tileMDO)) {
			return loadedTiles.get(tileMDO);
		} else if (unloadedTiles.containsKey(tileMDO)) {
			return unloadedTiles.get(tileMDO);
		} else {
			// I don't like accessing the global here, it's janky
			requestTile(MGlobal.assetManager, tileMDO);
			return getTile(tileMDO);
		}
		
	}
	
	/**
	 * Retrieves the required tile with data specified by MDO with given key, or
	 * creates it if not available.
	 * @param	mdoKey			The key of the data file to load from
	 * @return					The requested tile
	 */
	public TileType getTile(String mdoKey) {
		TileMDO mdo = MGlobal.data.getEntryFor(mdoKey, TileMDO.class);
		return getTile(mdo);
	}

}
