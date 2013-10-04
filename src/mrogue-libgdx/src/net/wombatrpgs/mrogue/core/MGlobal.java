/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.wombatrpgs.mrogue.characters.Hero;
import net.wombatrpgs.mrogue.core.reporters.*;
import net.wombatrpgs.mrogue.graphics.GraphicsSettings;
import net.wombatrpgs.mrogue.io.DefaultKeymap;
import net.wombatrpgs.mrogue.io.FileLoader;
import net.wombatrpgs.mrogue.io.Keymap;
import net.wombatrpgs.mrogue.io.loaders.DataLoader;
import net.wombatrpgs.mrogue.io.loaders.SceneLoader;
import net.wombatrpgs.mrogue.maps.LevelManager;
import net.wombatrpgs.mrogue.maps.gen.TileTypeManager;
import net.wombatrpgs.mrogue.scenes.SceneData;
import net.wombatrpgs.mrogue.scenes.TeleportGlobal;
import net.wombatrpgs.mrogue.screen.ScreenStack;
import net.wombatrpgs.mrogue.screen.WindowSettings;
import net.wombatrpgs.mrogue.screen.instances.GameScreen;
import net.wombatrpgs.mrogue.ui.UISettings;
import net.wombatrpgs.mrogueschema.settings.GraphicsSettingsMDO;
import net.wombatrpgs.mrogueschema.settings.TeleportSettingsMDO;
import net.wombatrpgs.mrogueschema.settings.UISettingsMDO;
import net.wombatrpgs.mrogueschema.settings.WindowSettingsMDO;

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
	/** My hero~~~~ <3 <3 <3 (the player's physical representation */
	public static Hero hero;
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
	/** Teleport settings, from database */
	public static TeleportGlobal teleport;
	/** Loader for simple text files */
	public static FileLoader loader;
	/** Manages all tile types and graphics etc */
	public static TileTypeManager tiles;
	
	private static List<Queueable> toLoad;
	
	/**
	 * Can't override static methods, so this thing will have to do.
	 */
	@Deprecated
	public static void setupRGlobalForTesting() {
		MGlobal.assetManager = new AssetManager();
		MGlobal.screens = new ScreenStack();
		MGlobal.keymap = new DefaultKeymap();
		MGlobal.rand = new Random(System.currentTimeMillis());
		//Global.setupGlobalForTesting();
	}
	
	/**
	 * Called as part of the map game.
	 */
	public static void globalInit() {
		
		// debugging is needed first
		MGlobal.reporter = new PrintReporter();
		try {
			MGlobal.reporter.inform("Initializing error reporting");
			MGlobal.assetManager = new AssetManager();
			MGlobal.reporter.inform("Initializing primary globals");
			MGlobal.rand = new Random(System.currentTimeMillis());
			MGlobal.data = new Database();
			
			// load up data marked essential, this will always be ugly
			MGlobal.reporter.inform("Loading essential data");
			setHandlers();
			MGlobal.data.queueData(assetManager, Constants.PRELOAD_SCHEMA);
			assetManager.finishLoading();
			
			// here on out, these may require essential data
			toLoad = new ArrayList<Queueable>();
			MGlobal.reporter.inform("Intializing secondary globals");
			MGlobal.constants = new Constants();
			MGlobal.screens = new ScreenStack();
			MGlobal.keymap = new DefaultKeymap();
			MGlobal.loader = new FileLoader();
			MGlobal.levelManager = new LevelManager();
			MGlobal.tiles = new TileTypeManager();
			
			// load secondary data
			// TODO: load with a loading bar
			MGlobal.reporter.inform("Loading secondary data");
			MGlobal.data.queueFilesInDir(assetManager, Gdx.files.internal(Constants.DATA_DIR));
			assetManager.finishLoading();
	
			// initialize everything that needed data
			MGlobal.reporter.inform("Initializing data-dependant resources");
			MGlobal.window = new WindowSettings(
					MGlobal.data.getEntryFor(Constants.WINDOW_KEY, WindowSettingsMDO.class));
			MGlobal.graphics = new GraphicsSettings(
					MGlobal.data.getEntryFor(Constants.GRAPHICS_KEY, GraphicsSettingsMDO.class));
			MGlobal.ui = new UISettings(MGlobal.data.getEntryFor(
					UISettings.DEFAULT_MDO_KEY, UISettingsMDO.class));
			MGlobal.teleport = new TeleportGlobal(MGlobal.data.getEntryFor(
					TeleportGlobal.DEFAULT_MDO_KEY, TeleportSettingsMDO.class));
			toLoad.add(ui);
			toLoad.add(teleport);
			toLoad.add(graphics);
			for (Queueable q : toLoad) q.queueRequiredAssets(assetManager);
			for (int pass = 0; MGlobal.assetManager.getProgress() < 1; pass++) {
				MGlobal.assetManager.finishLoading();
				for (Queueable q : toLoad) q.postProcessing(MGlobal.assetManager, pass);
			}
			
			// initializing graphics
			MGlobal.reporter.inform("Creating screen and graphics");
			toLoad.clear();
			String result = loader.getText(Constants.CONFIG_FILE);
			boolean fullscreen = result.indexOf("true") != -1;
			Gdx.graphics.setDisplayMode(
					MGlobal.window.getResolutionWidth(),
					MGlobal.window.getResolutionHeight(), 
					fullscreen);
			// TODO: adjust for available resilutuons
			for (DisplayMode disp : Gdx.graphics.getDisplayModes()) {
				System.out.println("w, h: " + disp.width +" , " + disp.height);
			}
			Gdx.graphics.setTitle(MGlobal.window.getTitle());
			//Gdx.graphics.setVSync(true);
			Gdx.input.setInputProcessor(MGlobal.keymap);
			
			MGlobal.reporter.inform("Loading level assets");
			MGlobal.screens.push(new GameScreen());
			toLoad.add(MGlobal.screens.peek());
			for (Queueable q : toLoad) q.queueRequiredAssets(assetManager);
			for (int pass = 0; MGlobal.assetManager.getProgress() < 1; pass++) {
				MGlobal.assetManager.finishLoading();
				for (Queueable q : toLoad) q.postProcessing(MGlobal.assetManager, pass);
			}
			
			initialized = true;
			MGlobal.reporter.inform("Done loading");
			
		} catch (Exception e) {
			// TODO: proper init error handling
			MGlobal.reporter.err("Exception during initialization: ", e);
			Gdx.app.exit();
		}
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
