/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

import net.wombatrpgs.rainfall.characters.Block;
import net.wombatrpgs.rainfall.characters.Hero;
import net.wombatrpgs.rainfall.core.reporters.PrintReporter;
import net.wombatrpgs.rainfall.io.DefaultKeymap;
import net.wombatrpgs.rainfall.io.Keymap;
import net.wombatrpgs.rainfall.io.loaders.DataLoader;
import net.wombatrpgs.rainfall.io.loaders.SceneLoader;
import net.wombatrpgs.rainfall.maps.LevelManager;
import net.wombatrpgs.rainfall.scenes.SceneData;
import net.wombatrpgs.rainfall.scenes.TeleportGlobal;
import net.wombatrpgs.rainfall.screens.ScreenStack;
import net.wombatrpgs.rainfall.ui.UISettings;
import net.wombatrpgs.rainfallschema.settings.TeleportSettingsMDO;
import net.wombatrpgs.rainfallschema.settings.UISettingsMDO;
import net.wombatrpgs.rainfallschema.settings.WindowSettingsMDO;

/**
 * Rainfall's version of the MGNDB global.
 */
public class RGlobal {
	
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
	/** Everybody's favorite petrified cashier */
	public static Block block;
	/** A single-source RNG */
	public static Random rand;
	/** The UI settings currently in use by the game */
	public static UISettings ui;
	/** Are we done loading yet? */
	public static boolean initialized = false;
	/** Our current window settings */
	public static WindowSettingsMDO window;
	/** Teleport settings, from database */
	public static TeleportGlobal teleport;
	
	private static List<Queueable> toLoad;
	
	/**
	 * Can't override static methods, so this thing will have to do.
	 */
	@Deprecated
	public static void setupRGlobalForTesting() {
		RGlobal.assetManager = new AssetManager();
		RGlobal.screens = new ScreenStack();
		RGlobal.keymap = new DefaultKeymap();
		RGlobal.rand = new Random(System.currentTimeMillis());
		//Global.setupGlobalForTesting();
	}
	
	/**
	 * Called as part of the map game.
	 */
	public static void globalInit() {
		
		// debugging is needed first
		RGlobal.reporter = new PrintReporter();
		RGlobal.reporter.inform("Initializing primary globals");
		RGlobal.assetManager = new AssetManager();
		RGlobal.rand = new Random(System.currentTimeMillis());
		RGlobal.data = new Database();
		
		// load up data marked essential, this will always be ugly
		RGlobal.reporter.inform("Loading essential data");
		setHandlers();
		RGlobal.data.queueData(assetManager, Constants.PRELOAD_SCHEMA);
		assetManager.finishLoading();
		
		// here on out, these may require essential data
		toLoad = new ArrayList<Queueable>();
		RGlobal.reporter.inform("Intializing secondary globals");
		RGlobal.constants = new Constants();
		RGlobal.screens = new ScreenStack();
		RGlobal.keymap = new DefaultKeymap();
		RGlobal.levelManager = new LevelManager();
		
		// load secondary data
		// TODO: load with a loading bar
		RGlobal.reporter.inform("Loading secondary data");
		RGlobal.data.queueFilesInDir(assetManager, Gdx.files.internal(Constants.DATA_DIR));
		assetManager.finishLoading();

		// initialize everything that needed data
		RGlobal.reporter.inform("Initializing data-dependant resources");
		RGlobal.window = RGlobal.data.getEntryFor(Constants.WINDOW_KEY, WindowSettingsMDO.class);
		RGlobal.ui = new UISettings(RGlobal.data.getEntryFor(
				UISettings.DEFAULT_MDO_KEY, UISettingsMDO.class));
		RGlobal.teleport = new TeleportGlobal(RGlobal.data.getEntryFor(
				TeleportGlobal.DEFAULT_MDO_KEY, TeleportSettingsMDO.class));
		toLoad.add(ui);
		toLoad.add(teleport);
		for (Queueable q : toLoad) q.queueRequiredAssets(assetManager);
		assetManager.finishLoading();
		for (Queueable q : toLoad) q.postProcessing(assetManager, 0);
		
		initialized = true;
		RGlobal.reporter.inform("Done loading");
	}
	
	/**
	 * Sets all the file handlers used by the asset manager.
	 */
	public static void setHandlers() {
		assetManager.setLoader(SceneData.class, new SceneLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(DataEntry.class, new DataLoader(new InternalFileHandleResolver()));
	}

}
