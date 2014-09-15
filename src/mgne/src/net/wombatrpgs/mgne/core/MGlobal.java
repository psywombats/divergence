/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.mgne.core.data.Database;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Reporter;
import net.wombatrpgs.mgne.core.lua.Lua;
import net.wombatrpgs.mgne.graphics.GraphicsSettings;
import net.wombatrpgs.mgne.io.MFiles;
import net.wombatrpgs.mgne.io.Keymap;
import net.wombatrpgs.mgne.io.audio.SoundManager;
import net.wombatrpgs.mgne.maps.LevelManager;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.screen.ScreenStack;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.UISettings;
import net.wombatrpgs.mgne.util.CharConverter;
import net.wombatrpgs.mgneschema.settings.UISettingsMDO;
import net.wombatrpgs.mgneschema.settings.WindowSettingsMDO;

/**
 * Rainfall's version of the MGNDB global.
 */
public class MGlobal {
	
	/** Core functionality */
	public static Reporter reporter;
	public static Database data;
	public static Memory memory;
	public static Lua lua;
	public static boolean initialized = false;
	
	/** Managers and factories */
	public static LevelManager levelManager;
	public static EventFactory eventFactory;
	public static SoundManager sfx;
	
	/** Screens */
	public static ScreenStack screens;
	public static Keymap keymap;
	
	/** Util */
	public static Random rand;
	public static Constants constants;
	public static CharConverter charConverter;
	
	/** Loaders */
	public static MAssets assets;
	public static MFiles files;
	
	/** Settings from the user */
	public static UISettings ui;
	public static WindowSettings window;
	public static GraphicsSettings graphics;
	
	/** Desktop mode */
	public static Map<String, String> args;
	public static Platform platform;
	public static MgnGame game;
	public static DebugLevel debug;
	
	/**
	 * Called as part of the map game.
	 */
	public static void globalInit() {
		
		// debugging is needed first
		MGlobal.reporter = platform.getReporter();
		try {
			long startTime = System.currentTimeMillis();
			MGlobal.reporter.inform("Initialized error reporting");
			MGlobal.assets = new MAssets();
			MGlobal.reporter.inform("Initializing primary globals");
			long seed = System.currentTimeMillis();
			MGlobal.rand = new Random(seed);
			MGlobal.reporter.inform("Using global seed " + seed);
			MGlobal.data = new Database();
			
			// load up data marked essential, this will always be ugly
			MGlobal.reporter.inform("Loading essential data");
			MGlobal.data.queueData(assets, Constants.PRELOAD_SCHEMA);
			long assetStart = System.currentTimeMillis();
			assets.finishLoading();
			long assetEnd = System.currentTimeMillis();
			float assetElapsed = (assetEnd - assetStart) / 1000f;
			MGlobal.reporter.inform("Finished loading essential data, " +
					"elapsed time: " + assetElapsed + "seconds");
			
			// here on out, these may require essential data
			List<Queueable> toLoad = new ArrayList<Queueable>();
			MGlobal.reporter.inform("Intializing secondary globals");
			MGlobal.constants = new Constants();
			MGlobal.screens = new ScreenStack();
			MGlobal.files = new MFiles();
			MGlobal.levelManager = new LevelManager();
			MGlobal.eventFactory = game.makeEventFactory();
			MGlobal.sfx = new SoundManager();
			
			// load secondary data
			// TODO: polish: load with a loading bar
			MGlobal.reporter.inform("Loading secondary data");
			MGlobal.data.queueFilesInDir(assets, Gdx.files.internal(Constants.DATA_DIR));
			assetStart = System.currentTimeMillis();
			assets.finishLoading();
			assetEnd = System.currentTimeMillis();
			assetElapsed = (assetEnd - assetStart) / 1000f;
			MGlobal.reporter.inform("Finished loading secondary data, " +
					"elapsed time: " + assetElapsed + "seconds");
	
			// initialize everything that needed data
			MGlobal.reporter.inform("Initializing data-dependant resources");
			MGlobal.game.onDataLoaded();
			MGlobal.window = new WindowSettings(
					MGlobal.data.getEntryFor(Constants.KEY_WINDOW, WindowSettingsMDO.class));
			MGlobal.graphics = game.makeGraphics();
			MGlobal.ui = new UISettings(MGlobal.data.getEntryFor(
					UISettings.DEFAULT_MDO_KEY, UISettingsMDO.class));
			MGlobal.keymap = Keymap.initDefaultKeymap();
			MGlobal.memory = game.makeMemory();
			MGlobal.charConverter = new CharConverter();
			MGlobal.lua = new Lua();
			toLoad.add(ui);
			toLoad.add(graphics);
			assets.loadAssets(toLoad, "primary global assets");
			
			// now the game itself can be created
			game.onCreate();
			
			// initializing graphics
			MGlobal.reporter.inform("Creating level-dependant data");
			toLoad.clear();
			String result = files.getText(Constants.CONFIG_FILE);
			boolean fullscreen = result.indexOf("true") != -1;
			Gdx.graphics.setDisplayMode(
					MGlobal.window.getWidth(),
					MGlobal.window.getHeight(), 
					fullscreen);
			MGlobal.screens.push(game.makeStarterScreen());
			toLoad.add(MGlobal.screens.peek());
			Gdx.graphics.setTitle(MGlobal.window.getTitle());
			
			assets.loadAssets(toLoad, "level assets");
			
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
	 * Static disposal call.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	public static void dispose() {
		screens.dispose();
		assets.dispose();
		graphics.dispose();
		sfx.dispose();
	}

	/**
	 * Replacement for the public static field. This gets the hero from the
	 * game screen, which is owned by the level manager.
	 * @return					The player avatar on the world map
	 */
	public static Avatar getHero() {
		return levelManager.getHero();
	}

}
