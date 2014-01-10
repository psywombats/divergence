/**
 *  LevelManager.java
 *  Created on Dec 25, 2012 5:54:15 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.scenes.SceneFactory;
import net.wombatrpgs.saga.scenes.SceneParser;
import net.wombatrpgs.saga.scenes.TeleportManager;
import net.wombatrpgs.saga.screen.Screen;
import net.wombatrpgs.sagaschema.cutscene.data.SceneParentMDO;
import net.wombatrpgs.sagaschema.maps.GeneratedMapMDO;
import net.wombatrpgs.sagaschema.maps.LoadedMapMDO;
import net.wombatrpgs.sagaschema.maps.data.MapMDO;
import net.wombatrpgs.sagaschema.settings.TeleportSettingsMDO;

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
			teleport = new TeleportManager(SGlobal.data.getEntryFor(
					TeleportManager.MD0_KEY, TeleportSettingsMDO.class));
			teleport.queueRequiredAssets(SGlobal.assetManager);
		}
		if (!levels.containsKey(mapID)) {
			// TODO: figure out this tint bullshit and why it's needed
			// it's buggy, this shouldn't be necessary
			float oldR = 0, oldG = 0, oldB = 0;
			if (SGlobal.screens.size() > 0) {
				oldR = SGlobal.screens.peek().getTint().r;
				oldG = SGlobal.screens.peek().getTint().g;
				oldB = SGlobal.screens.peek().getTint().b;
				SGlobal.screens.peek().getTint().r = 1;
				SGlobal.screens.peek().getTint().g = 1;
				SGlobal.screens.peek().getTint().b = 1;
			}
			MapMDO mapMDO = SGlobal.data.getEntryFor(mapID, MapMDO.class);
			Level map = createMap(mapMDO, screen);
			long startTime = System.currentTimeMillis();
			map.queueRequiredAssets(SGlobal.assetManager);
			for (int pass = 0; SGlobal.assetManager.getProgress() < 1; pass++) {
				SGlobal.assetManager.finishLoading();
				map.postProcessing(SGlobal.assetManager, pass);
				teleport.postProcessing(SGlobal.assetManager, pass);
			}
			long endTime = System.currentTimeMillis();
			float elapsed  = (endTime - startTime) / 1000f;
			SGlobal.reporter.inform("Loaded level " + mapID + ", elapsed " +
						"time: " + elapsed + " seconds");
			levels.put(mapID, map);
			if (SGlobal.screens.size() > 0) {
				SGlobal.screens.peek().getTint().r = oldR;
				SGlobal.screens.peek().getTint().g = oldG;
				SGlobal.screens.peek().getTint().b = oldB;
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
		SceneParentMDO mdo = SGlobal.data.getEntryFor(mdoKey, SceneParentMDO.class);
		return cutsceneGen.createScene(mdo, other);
	}
	
	/**
	 * Our own internal level factory, now that there are two kinds of levels.
	 * @param	mdo				The map mdo, either generated or loaded
	 * @param	screen			The game screen to generate for, I think
	 * @return					The created map, without assets loaded
	 */
	private static Level createMap(MapMDO mdo, Screen screen) {
		if (GeneratedMapMDO.class.isAssignableFrom(mdo.getClass())) {
			return new GeneratedLevel((GeneratedMapMDO) mdo, screen);
		} else if (LoadedMapMDO.class.isAssignableFrom(mdo.getClass())) {
			return new LoadedLevel((LoadedMapMDO) mdo, screen);
		} else {
			SGlobal.reporter.err("Unknown subtype of mapmdo :" + mdo.getClass());
			return null;
		}
	}
	
}
