/**
 *  RGlobal.java
 *  Created on Nov 11, 2012 3:08:03 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.global.Global;

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
	
	/**
	 * Can't override static methods, so this thing will have to do.
	 */
	public static void setupRGlobalForTesting() {
		RGlobal.assetManager = new AssetManager();
		Global.setupGlobalForTesting();
	}

}
