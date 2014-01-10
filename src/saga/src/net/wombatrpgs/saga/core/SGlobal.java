/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.wombatrpgs.saga.graphics.GraphicsSettings;
import net.wombatrpgs.saga.io.DefaultKeymap;
import net.wombatrpgs.saga.io.FileLoader;
import net.wombatrpgs.saga.io.Keymap;
import net.wombatrpgs.saga.io.loaders.DataLoader;
import net.wombatrpgs.saga.io.loaders.SceneLoader;
import net.wombatrpgs.saga.maps.LevelManager;
import net.wombatrpgs.saga.maps.gen.TileManager;
import net.wombatrpgs.saga.rpg.Avatar;
import net.wombatrpgs.saga.scenes.SceneData;
import net.wombatrpgs.saga.screen.ScreenStack;
import net.wombatrpgs.saga.screen.WindowSettings;
import net.wombatrpgs.saga.screen.instances.GameScreen;
import net.wombatrpgs.saga.ui.UISettings;
import net.wombatrpgs.sagaschema.settings.GraphicsSettingsMDO;
import net.wombatrpgs.sagaschema.settings.UISettingsMDO;
import net.wombatrpgs.sagaschema.settings.WindowSettingsMDO;

/**
 * Rainfall's version of the MGNDB global.
 */
public class SGlobal {
	
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
	/** Manages all tile types and graphics etc */
	public static TileManager tiles;
	/** Desktop mode */
	public static Platform platform;
	/** ZS2 weird shit */
	public static boolean raveMode, stasis, won, won2;
	public static int deathCount;
	
	private static List<Queueable> toLoad;
	
	/**
	 * Can't override static methods, so this thing will have to do.
	 */
	@Deprecated
	public static void setupRGlobalForTesting() {
		SGlobal.assetManager = new AssetManager();
		SGlobal.screens = new ScreenStack();
		SGlobal.keymap = new DefaultKeymap();
		SGlobal.rand = new Random(System.currentTimeMillis());
		//Global.setupGlobalForTesting();
	}
	
	/**
	 * Called as part of the map game.
	 */
	public static void globalInit() {
		
		// debugging is needed first
		SGlobal.reporter = platform.getReporter();
		try {
			long startTime = System.currentTimeMillis();
			SGlobal.reporter.inform("Initialized error reporting");
			SGlobal.assetManager = new AssetManager();
			SGlobal.reporter.inform("Initializing primary globals");
			long seed = System.currentTimeMillis();
			SGlobal.rand = new Random(seed);
			SGlobal.reporter.inform("Using global seed " + seed);
			SGlobal.data = new Database();
			SGlobal.keymap = new DefaultKeymap();
			
			// load up data marked essential, this will always be ugly
			SGlobal.reporter.inform("Loading essential data");
			setHandlers();
			SGlobal.data.queueData(assetManager, Constants.PRELOAD_SCHEMA);
			long assetStart = System.currentTimeMillis();
			assetManager.finishLoading();
			long assetEnd = System.currentTimeMillis();
			float assetElapsed = (assetEnd - assetStart) / 1000f;
			SGlobal.reporter.inform("Finished loading essential data, " +
					"elapsed time: " + assetElapsed + "seconds");
			
			// here on out, these may require essential data
			toLoad = new ArrayList<Queueable>();
			SGlobal.reporter.inform("Intializing secondary globals");
			SGlobal.constants = new Constants();
			SGlobal.screens = new ScreenStack();
			SGlobal.loader = new FileLoader();
			SGlobal.tiles = new TileManager();
			SGlobal.levelManager = new LevelManager();
			
			// load secondary data
			// TODO: load with a loading bar
			SGlobal.reporter.inform("Loading secondary data");
			SGlobal.data.queueFilesInDir(assetManager, Gdx.files.internal(Constants.DATA_DIR));
			assetEnd = System.currentTimeMillis();
			assetManager.finishLoading();
			assetEnd = System.currentTimeMillis();
			SGlobal.reporter.inform("Finished loading secondary data, " +
					"elapsed time: " + assetElapsed + "seconds");
	
			// initialize everything that needed data
			SGlobal.reporter.inform("Initializing data-dependant resources");
			SGlobal.window = new WindowSettings(
					SGlobal.data.getEntryFor(Constants.KEY_WINDOW, WindowSettingsMDO.class));
			SGlobal.graphics = new GraphicsSettings(
					SGlobal.data.getEntryFor(Constants.KEY_GRAPHICS, GraphicsSettingsMDO.class));
			SGlobal.ui = new UISettings(SGlobal.data.getEntryFor(
					UISettings.DEFAULT_MDO_KEY, UISettingsMDO.class));
			toLoad.add(ui);
			toLoad.add(graphics);
			for (Queueable q : toLoad) q.queueRequiredAssets(assetManager);
			for (int pass = 0; SGlobal.assetManager.getProgress() < 1; pass++) {
				assetStart = System.currentTimeMillis();
				SGlobal.assetManager.finishLoading();
				assetEnd = System.currentTimeMillis();
				assetElapsed = (assetEnd - assetStart) / 1000f;
				SGlobal.reporter.inform("Loading pass " + pass + ", took " + assetElapsed);
				for (Queueable q : toLoad) q.postProcessing(SGlobal.assetManager, pass);
			}
			
			// initializing graphics
			SGlobal.reporter.inform("Creating level-dependant data");
			toLoad.clear();
			String result = loader.getText(Constants.CONFIG_FILE);
			boolean fullscreen = result.indexOf("true") != -1;
			Gdx.graphics.setDisplayMode(
					SGlobal.window.getResolutionWidth(),
					SGlobal.window.getResolutionHeight(), 
					fullscreen);
			SGlobal.screens.push(new GameScreen());
			Gdx.graphics.setTitle(SGlobal.window.getTitle());
			//Gdx.graphics.setVSync(true);
			Gdx.input.setInputProcessor(SGlobal.keymap);
			
			SGlobal.reporter.inform("Loading level assets");
			for (Queueable q : toLoad) q.queueRequiredAssets(assetManager);
			for (int pass = 0; SGlobal.assetManager.getProgress() < 1; pass++) {
				SGlobal.reporter.inform("Loading pass " + pass + ", took " + assetElapsed);
				SGlobal.assetManager.finishLoading();
				for (Queueable q : toLoad) q.postProcessing(SGlobal.assetManager, pass);
				assetEnd = System.currentTimeMillis();
				assetElapsed = (assetEnd - assetStart) / 1000f;
			}
			
			initialized = true;
			long endTime = System.currentTimeMillis();
			float elapsed = (endTime - startTime) / 1000f;
			SGlobal.reporter.inform("Done loading, elasped time: " + elapsed + 
					" seconds");
			
		} catch (Exception e) {
			// TODO: proper init error handling
			SGlobal.reporter.err("Exception during initialization: ", e);
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
	}

}
