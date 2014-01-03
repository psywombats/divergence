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
import net.wombatrpgs.mrogue.scenes.SceneFactory;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.scenes.TeleportManager;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;
import net.wombatrpgs.mrogueschema.maps.MapMDO;
import net.wombatrpgs.mrogueschema.settings.TeleportSettingsMDO;

/**
 * Manages levels so that they don't have to be created multiple times. It
 * should probably support asynchronous loading at some point. This... This is
 * probably a memory hog.
 */
public class LevelManager {
	
	/** Goes from map IDs to their level manifestation */
	protected Map<String, Level> levels;
	protected Screen screen;
	protected SceneFactory cutsceneGen;
	protected Level active;
	protected TeleportManager teleport;
	
	/**
	 * Creates and initializes a new level manager.
	 */
	public LevelManager() {
		levels = new HashMap<String, Level>();
		cutsceneGen = new SceneFactory();
	}
	
	/** @param screen The screen that will be showing levels */
	public void setScreen(Screen screen) { this.screen = screen; }
	
	/** @return The screen levels use */
	public Screen getScreen() { return screen; }
	
	/** @return The currently active level */
	public Level getActive() { return active; }
	
	/** @param active The new active level */
	public void setActive(Level active) { this.active = active; }
	
	/** @return The teleport processor for these levels */
	public TeleportManager getTele() { return this.teleport; }
	
	/** @return The name of the hero in these levels */
	public String getHeroName() { return cutsceneGen.getHeroName(); }
	
	/** @return The name of the boss in these levels */
	public String getBossName() { return cutsceneGen.getBossName(); }
	
	/**
	 * Resets like it's a new game.
	 */
	public void reset() {
		screen = null;
		active = null;
		teleport = null;
		levels.clear();
		levels = new HashMap<String, Level>();
		cutsceneGen = new SceneFactory();
	}
	
	/**
	 * Converts a string id into a level, either by fetching it or loading it
	 * up. WARNING: right now it loads the entire goddamn level if it hasn't
	 * been loaded yet.
	 * @param 	mapID		The map id to load up
	 * @return				The map, either gen'd or stored
	 */
	public Level getLevel(String mapID) {
		if (teleport == null) {
			teleport = new TeleportManager(MGlobal.data.getEntryFor(
					TeleportManager.MD0_KEY, TeleportSettingsMDO.class));
			teleport.queueRequiredAssets(MGlobal.assetManager);
		}
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
			long startTime = System.currentTimeMillis();
			map.queueRequiredAssets(MGlobal.assetManager);
			for (int pass = 0; MGlobal.assetManager.getProgress() < 1; pass++) {
				MGlobal.assetManager.finishLoading();
				map.postProcessing(MGlobal.assetManager, pass);
				teleport.postProcessing(MGlobal.assetManager, pass);
			}
			long endTime = System.currentTimeMillis();
			float elapsed  = (endTime - startTime) / 1000f;
			MGlobal.reporter.inform("Loaded level " + mapID + ", elapsed " +
						"time: " + elapsed + " seconds");
			levels.put(mapID, map);
			if (MGlobal.screens.size() > 0) {
				MGlobal.screens.peek().getTint().r = oldR;
				MGlobal.screens.peek().getTint().g = oldG;
				MGlobal.screens.peek().getTint().b = oldB;
			}
		}
		return levels.get(mapID);
	}
	
	/**
	 * Children should call this to create their cutscenes.
	 * @param	mdoKey			The key of the mdo of the scen to generate
	 * @return					A scene from that mdo
	 */
	public SceneParser getCutscene(String mdoKey) {
		return getCutscene(mdoKey, getScreen());
	}
	
	/**
	 * Children should call this to create their cutscenes.
	 * @param	mdoKey			The key of the mdo of the scen to generate
	 * @param	other			The screen to generate for
	 * @return					A scene from that mdo
	 */
	public SceneParser getCutscene(String mdoKey, Screen other) {
		SceneParentMDO mdo = MGlobal.data.getEntryFor(mdoKey, SceneParentMDO.class);
		return cutsceneGen.createScene(mdo, other);
	}
	
}
