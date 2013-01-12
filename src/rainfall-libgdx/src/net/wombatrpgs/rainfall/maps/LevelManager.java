/**
 *  LevelManager.java
 *  Created on Dec 25, 2012 5:54:15 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.maps.MapMDO;

/**
 * Manages levels so that they don't have to be created multiple times. It
 * should probably support asynchronous loading at some point.
 */
public class LevelManager {
	
	/** Goes from map IDs to their level manifestation */
	protected Map<String, Level> levels;
	
	/**
	 * Creates and initializes a new level manager.
	 */
	public LevelManager() {
		levels = new HashMap<String, Level>();
	}
	
	/**
	 * Converts a string id into a level, either by fetching it or loading it
	 * up.
	 * @param 	mapID		The map id to load up
	 * @return				The map, either gen'd or stored
	 */
	public Level getLevel(String mapID) {
		if (!levels.containsKey(mapID)) {
			MapMDO mapMDO = RGlobal.data.getEntryFor(mapID, MapMDO.class);
			Level map = new Level(mapMDO);
			map.queueRequiredAssets(RGlobal.assetManager);
			while (!RGlobal.assetManager.update());
			map.postProcessing(RGlobal.assetManager);
			map.queueMapObjectAssets(RGlobal.assetManager);
			while (!RGlobal.assetManager.update());
			map.postProcessingMapObjects(RGlobal.assetManager);
			levels.put(mapID, map);
		}
		return levels.get(mapID);
	}

}
