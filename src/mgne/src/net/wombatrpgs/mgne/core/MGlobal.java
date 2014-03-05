/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.luaj.vm2.LuaValue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.wombatrpgs.mgne.core.data.DataEntry;
import net.wombatrpgs.mgne.core.data.Database;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Reporter;
import net.wombatrpgs.mgne.core.lua.Lua;
import net.wombatrpgs.mgne.graphics.GraphicsSettings;
import net.wombatrpgs.mgne.io.FileLoader;
import net.wombatrpgs.mgne.io.Keymap;
import net.wombatrpgs.mgne.io.loaders.DataLoader;
import net.wombatrpgs.mgne.io.loaders.LuaLoader;
import net.wombatrpgs.mgne.io.loaders.SceneLoader;
import net.wombatrpgs.mgne.maps.LevelManager;
import net.wombatrpgs.mgne.scenes.SceneData;
import net.wombatrpgs.mgne.screen.ScreenStack;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.UISettings;
import net.wombatrpgs.mgneschema.settings.GraphicsSettingsMDO;
import net.wombatrpgs.mgneschema.settings.UISettingsMDO;
import net.wombatrpgs.mgneschema.settings.WindowSettingsMDO;

/**
 * Rainfall's version of the MGNDB global.
 */
public class MGlobal {
	
	/** Error-reporting dispatcher */
	public static Reporter reporter;
	/** Storage container for data entries */
	public static Database data;
	/** Manages all in-game assets */
	public static AssetManager assetManager;
	/** The stack of screeeeeeens */
	public static ScreenStack screens;
	/** Current mapper of the keyys */
	public static Keymap keymap;
	/** All magic numbers and stuff */
	public static Constants constants;
	/** Stores all of our levels */
	public static LevelManager levelManager;
	/** A single-source RNG */
	public static Random rand;
	/** The UI settings currently in use by the game */
	public static UISettings ui;
	/** Are we done loading yet? */
	public static boolean initialized = false;
	/** Our current window settings */
	public static WindowSettings window;
	/** Our current graphics settings */
	public static GraphicsSettings graphics;
	/** Loader for simple text files */
	public static FileLoader loader;
	/** Desktop mode */
	public static Platform platform;
	/** Switches and variables */
	public static Memory memory;
	/** L-l-l-lua?? */
	public static Lua lua;
	/** Game-specific information and hooks */
	public static MgnGame game;
	
	private static List<Queueable> toLoad;
	
	/**
	 * Called as part of the map game.
	 */
	public static void globalInit() {
		
		// debugging is needed first
		MGlobal.reporter = platform.getReporter();
		try {
			long startTime = System.currentTimeMillis();
			MGlobal.reporter.inform("Initialized error reporting");
			MGlobal.assetManager = new AssetManager();
			MGlobal.reporter.inform("Initializing primary globals");
			long seed = System.currentTimeMillis();
			MGlobal.rand = new Random(seed);
			MGlobal.reporter.inform("Using global seed " + seed);
			MGlobal.data = new Database();
			
			// load up data marked essential, this will always be ugly
			MGlobal.reporter.inform("Loading essential data");
			setHandlers();
			MGlobal.data.queueData(assetManager, Constants.PRELOAD_SCHEMA);
			long assetStart = System.currentTimeMillis();
			assetManager.finishLoading();
			long assetEnd = System.currentTimeMillis();
			float assetElapsed = (assetEnd - assetStart) / 1000f;
			MGlobal.reporter.inform("Finished loading essential data, " +
					"elapsed time: " + assetElapsed + "seconds");
			
			// here on out, these may require essential data
			toLoad = new ArrayList<Queueable>();
			MGlobal.reporter.inform("Intializing secondary globals");
			MGlobal.constants = new Constants();
			MGlobal.screens = new ScreenStack();
			MGlobal.loader = new FileLoader();
//			SGlobal.tiles = new TileManager();
			MGlobal.levelManager = new LevelManager();
			
			// load secondary data
			// TODO: polish: load with a loading bar
			MGlobal.reporter.inform("Loading secondary data");
			MGlobal.data.queueFilesInDir(assetManager, Gdx.files.internal(Constants.DATA_DIR));
			assetStart = System.currentTimeMillis();
			assetManager.finishLoading();
			assetEnd = System.currentTimeMillis();
			assetElapsed = (assetEnd - assetStart) / 1000f;
			MGlobal.reporter.inform("Finished loading secondary data, " +
					"elapsed time: " + assetElapsed + "seconds");
	
			// initialize everything that needed data
			MGlobal.reporter.inform("Initializing data-dependant resources");
			MGlobal.window = new WindowSettings(
					MGlobal.data.getEntryFor(Constants.KEY_WINDOW, WindowSettingsMDO.class));
			MGlobal.graphics = new GraphicsSettings(
					MGlobal.data.getEntryFor(Constants.KEY_GRAPHICS, GraphicsSettingsMDO.class));
			MGlobal.ui = new UISettings(MGlobal.data.getEntryFor(
					UISettings.DEFAULT_MDO_KEY, UISettingsMDO.class));
			MGlobal.keymap = Keymap.initDefaultKeymap();
			// TODO: persistence: memory should persist
			MGlobal.memory = new Memory();
			MGlobal.lua = new Lua();
			toLoad.add(ui);
			toLoad.add(graphics);
			loadAssets(toLoad, "primary global assets");
			
			// initializing graphics
			MGlobal.reporter.inform("Creating level-dependant data");
			toLoad.clear();
			String result = loader.getText(Constants.CONFIG_FILE);
			boolean fullscreen = result.indexOf("true") != -1;
			Gdx.graphics.setDisplayMode(
					MGlobal.window.getResolutionWidth(),
					MGlobal.window.getResolutionHeight(), 
					fullscreen);
			MGlobal.screens.push(game.makeStarterScreen());
			Gdx.graphics.setTitle(MGlobal.window.getTitle());
			//Gdx.graphics.setVSync(true);
			
			loadAssets(toLoad, "level assets");
			
			initialized = true;
			long endTime = System.currentTimeMillis();
			float elapsed = (endTime - startTime) / 1000f;
			MGlobal.reporter.inform("Done loading, elasped time: " + elapsed + 
					" seconds");
			
		} catch (Exception e) {
			// TODO: polish: proper init error handling
			MGlobal.reporter.err("Exception during initialization: ", e);
			Gdx.app.exit();
		}
	}
	
	/**
	 * Replacement for the public static field. This gets the hero from the
	 * game screen, which is owned by the level manager.
	 * @return					The player avatar on the world map
	 */
	public static Avatar getHero() {
		return levelManager.getScreen().getHero();
	}
	
	/**
	 * Sets all the file handlers used by the asset manager.
	 */
	public static void setHandlers() {
		assetManager.setLoader(SceneData.class, new SceneLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(DataEntry.class, new DataLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(LuaValue.class, new LuaLoader(new InternalFileHandleResolver()));
	}
	
	/**
	 * Queues, loads queued assets. Blocks.
	 * @param	toLoad			All assets to load
	 * @param	name			The name of what is being loadded
	 */
	public static void loadAssets(List<Queueable> toLoad, String name) {
		for (Queueable q : toLoad) q.queueRequiredAssets(assetManager);
		int pass;
		for (pass = 0; assetManager.getProgress() < 1; pass++) {
			float assetStart = System.currentTimeMillis();
			MGlobal.assetManager.finishLoading();
			float assetEnd = System.currentTimeMillis();
			float assetElapsed = (assetEnd - assetStart) / 1000f;
			for (Queueable q : toLoad) q.postProcessing(MGlobal.assetManager, pass);
			MGlobal.reporter.inform("Loading " + name + " pass " + pass + ", took " + assetElapsed);
		}
		if (pass == 0) {
			for (Queueable q : toLoad) q.postProcessing(MGlobal.assetManager, pass);
		}
	}
	
	/**
	 * Loads a single asset. Blocks.
	 * @param	toLoad			The thing to load
	 * @param	name			The name of the thing
	 */
	public static void loadAsset(Queueable toLoad, String name) {
		List<Queueable> dummy = new ArrayList<Queueable>();
		dummy.add(toLoad);
		loadAssets(dummy, name);
	}

}
