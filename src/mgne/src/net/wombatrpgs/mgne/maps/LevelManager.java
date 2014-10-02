/**
 *  LevelManager.java
 *  Created on Dec 25, 2012 5:54:15 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mgne.core.Avatar;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.scenes.TeleportManager;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;
import net.wombatrpgs.mgneschema.maps.data.MapMDO;
import net.wombatrpgs.mgneschema.maps.gen.GeneratedMapMDO;
import net.wombatrpgs.mgneschema.settings.TeleportSettingsMDO;

/**
 * Manages levels so that they don't have to be created multiple times. It
 * should probably support asynchronous loading at some point. This... This is
 * probably a memory hog.
 */
public class LevelManager implements Disposable {
	
	/** Goes from map IDs to their level manifestation */
	protected Map<String, Level> levels;
	protected Screen screen;
	protected Avatar hero;
	protected Level active;
	protected TeleportManager teleport;
	
	/**
	 * Creates and initializes a new level manager.
	 */
	public LevelManager() {
		levels = new HashMap<String, Level>();
	}
	
	/**@param screen The screen that will be showing levels */
	public void setScreen(Screen screen) { this.screen = screen; }
	
	/** @return The screen levels use */
	public Screen getScreen() { return screen; }
	
	/** @return The currently active level */
	public Level getActive() { return active; }
	
	/** @param active The new active level */
	public void setActive(Level active) { this.active = active; }
	
	/** @return The hero of this level set! */
	public Avatar getHero() { return hero; }
	
	/** @return The teleport processor for these levels */
	public TeleportManager getTele() {
		if (teleport == null) {
			teleport = new TeleportManager(MGlobal.data.getEntryFor(
					TeleportManager.MDO_KEY, TeleportSettingsMDO.class));
			MGlobal.assets.loadAsset(teleport, "teleport global");
		}
		return teleport;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (Level level : levels.values()) {
			level.dispose();
		}
	}

	/**
	 * Resets like it's a new game.
	 */
	public void reset() {
		screen = null;
		active = null;
		teleport = null;
		for (Level level : levels.values()) {
			level.dispose();
		}
		levels.clear();
	}
	
	/**
	 * Converts a string id into a level, either by fetching it or loading it
	 * up. WARNING: right now it loads the entire goddamn level if it hasn't
	 * been loaded yet.
	 * @param 	mapID		The map id to load up
	 * @return				The map, either gen'd or stored
	 */
	public Level getLevel(String mapID) {
		return getLevel(mapID, MGlobal.assets);
	}
	
	/**
	 * Set a new active hero and map. This should perform all the stuff found
	 * in a raw teleport to get the hero on to the map. Assume the hero is
	 * currently in limbo on no map. The hero's location will be set later.
	 * @param	hero			The hero to become active
	 * @param	map				The map to become active
	 */
	public void setNewActiveSet(Avatar hero, Level map) {
		this.hero = hero;
		getTele().teleportRaw(map, hero.getTileX(), hero.getTileY());
		getScreen().getCamera().track(MGlobal.getHero());
		getScreen().getCamera().update(0);
	}

	/**
	 * The old getLevel code, but can load with any loader. Use the default
	 * overloaded one to load from global default.
	 * @param	mapID			The map id to load up
	 * @param	loader			The loader to use assets from
	 * @return					The map, either gen'd or stored
	 */
	protected Level getLevel(String mapID, MAssets loader) {
		if (!levels.containsKey(mapID)) {
			MapMDO mapMDO = getLevelMDO(mapID);
			Level map = createMap(mapMDO, screen);
			loader.loadAsset(map, "Map assets, id: " + mapID);
			levels.put(mapID, map);
		}
		return levels.get(mapID);
	}
	
	/**
	 * Our own internal level factory, now that there are two kinds of levels.
	 * @param	mdo				The map mdo, either generated or loaded
	 * @param	screen			The game screen to generate for, I think
	 * @return					The created map, without assets loaded
	 */
	protected static Level createMap(MapMDO mdo, Screen screen) {
		if (GeneratedMapMDO.class.isAssignableFrom(mdo.getClass())) {
//			return new GeneratedLevel((GeneratedMapMDO) mdo, screen);
			MGlobal.reporter.err("Generated maps not yet supported");
			return null;
		} else if (LoadedMapMDO.class.isAssignableFrom(mdo.getClass())) {
			return new LoadedLevel((LoadedMapMDO) mdo, screen);
		} else {
			MGlobal.reporter.err("Unknown subtype of mapmdo :" + mdo.getClass());
			return null;
		}
	}
	
	/**
	 * Converts a string into an mdo, either by constructing a level for the
	 * named map or by retrieving from the database.
	 * @param	name			The name of the MDO or .tmx map
	 * @return					An MDO suitable for that name
	 */
	protected static MapMDO getLevelMDO(String name) {
		MapMDO mdo = MGlobal.data.getIfExists(name, MapMDO.class);
		if (mdo == null) {
			LoadedMapMDO loadedMDO = new LoadedMapMDO();
			loadedMDO.bgm = null;
			loadedMDO.effect = null;
			loadedMDO.file = name;
			mdo = loadedMDO;
		}
		return mdo;
	}
	
}
