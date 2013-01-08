/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.core;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.characters.Block;
import net.wombatrpgs.rainfall.characters.Hero;
import net.wombatrpgs.rainfall.io.DefaultKeymap;
import net.wombatrpgs.rainfall.io.Keymap;
import net.wombatrpgs.rainfall.maps.LevelManager;

/**
 * Rainfall's version of the MGNDB global.
 */
public class RGlobal extends Global {
	
	/** Directory where all resources are stored (prefix string) */
	public static final String RESOURCE_DIR = "res/";
	/** Directory where sprites are stored, prefix string */
	public static final String SPRITES_DIR = RESOURCE_DIR + "sprites/";
	/** Directory where maps are stored, prefix string */
	public static final String MAPS_DIR = RESOURCE_DIR + "maps/";
	
	/** Manages all in-game assets */
	public static AssetManager assetManager;
	
	/** The stack of screeeeeeens */
	public static net.wombatrpgs.rainfall.core.ScreenStack screens;
	
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
	
	/**
	 * Can't override static methods, so this thing will have to do.
	 */
	public static void setupRGlobalForTesting() {
		RGlobal.assetManager = new AssetManager();
		RGlobal.screens = new ScreenStack();
		RGlobal.keymap = new DefaultKeymap();
		Global.setupGlobalForTesting();
	}

}
