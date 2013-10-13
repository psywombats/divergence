/**
 *  LevelManager.java
 *  Created on Dec 25, 2012 5:54:15 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.maps.MapMDO;

/**
 * Manages levels so that they don't have to be created multiple times. It
 * should probably support asynchronous loading at some point.
 */
public class LevelManager {
	
	/** Goes from map IDs to their level manifestation */
	protected Map<String, Level> levels;
	/** The screen that will be showing all the levels */
	protected Screen screen;
	
	/**
	 * Creates and initializes a new level manager.
	 */
	public LevelManager() {
		levels = new HashMap<String, Level>();
	}
	
	/**
	 * Levels will render to the new screen.
	 * @param screen			The screen that will be showing levels
	 */
	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	
	/**
	 * Gets the screen levels use.
	 * @return =				The screen levels use
	 */
	public Screen getScreen() {
		return screen;
	}
	
	/**
	 * Converts a string id into a level, either by fetching it or loading it
	 * up. WARNING: right now it loads the entire goddamn level if it hasn't
	 * been loaded yet.
	 * @param 	mapID		The map id to load up
	 * @return				The map, either gen'd or stored
	 */
	// TODO: make this pretty loading
	public Level getLevel(String mapID) {
		if (!levels.containsKey(mapID)) {
			// TODO: figure out this tint bullshit and why it's needed
			// it's buggy, this shouldn't be necessary
			float oldR = 0, oldG = 0, oldB = 0;
			if (MGlobal.screens.size() > 0) {
				oldR = MGlobal.screens.peek().getTint().r;
				oldG = MGlobal.screens.peek().getTint().g;
				oldB = MGlobal.screens.peek().getTint().b;
				MGlobal.screens.peek().getTint().r = 1;
				MGlobal.screens.peek().getTint().g = 1;
				MGlobal.screens.peek().getTint().b = 1;
			}
			MapMDO mapMDO = MGlobal.data.getEntryFor(mapID, MapMDO.class);
			Level map = new Level(mapMDO, screen);
			map.queueRequiredAssets(MGlobal.assetManager);
			for (int pass = 0; MGlobal.assetManager.getProgress() < 1; pass++) {
				MGlobal.assetManager.finishLoading();
				map.postProcessing(MGlobal.assetManager, pass);
			}
			levels.put(mapID, map);
			if (MGlobal.screens.size() > 0) {
				MGlobal.screens.peek().getTint().r = oldR;
				MGlobal.screens.peek().getTint().g = oldG;
				MGlobal.screens.peek().getTint().b = oldB;
			}
		} else {
			levels.get(mapID).reset();
		}
		return levels.get(mapID);
	}

}
