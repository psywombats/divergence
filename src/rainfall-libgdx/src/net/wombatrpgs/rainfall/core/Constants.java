/**
 *  Constants.java
 *  Created on Nov 25, 2012 12:39:01 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.settings.GameSpeedMDO;
import net.wombatrpgs.rainfallschema.settings.UISettingsMDO;
import net.wombatrpgs.rainfallschema.settings.WindowSettingsMDO;

/**
 * Access to a bunch of magic numbers that are needed in a bunch of different
 * places. The idea is to only load up the schema once. Also holds other non-
 * database defined constants, or things that should be database constants
 * eventually.
 */
public class Constants {
	
	/** Directory where all resources are stored (prefix string) */
	public static final String RESOURCE_DIR = "res/";
	/** Directory where sprites are stored, prefix string */
	public static final String SPRITES_DIR = RESOURCE_DIR + "sprites/";
	/** Directory where maps are stored, prefix string */
	public static final String MAPS_DIR = RESOURCE_DIR + "maps/";
	/** Directory where all the gibs are stored, prefix string */
	public static final String GIBS_DIR = SPRITES_DIR;
	/** Uhhh where da right fonts at? */
	public static final String FONTS_DIR = RESOURCE_DIR + "fonts/";
	/** I hate my life */
	public static final String UI_DIR = RESOURCE_DIR + "ui/";
	/** Direction where all the scenes are stored, prefix string */
	public static final String SCENES_DIR = RESOURCE_DIR + "scenes/";
	/** Directory where all the .json mdos are stored, prefix string */
	public static final String DATA_DIR = RESOURCE_DIR + "data/";
	/** Directory where all the audio files are stored, prefix string */
	public static final String AUDIO_DIR = RESOURCE_DIR + "audio/";
	/** Directory where all the shaders are stored, prefix string */
	public static final String SHADERS_DIR = RESOURCE_DIR + "shaders/";
	
	/** The key of the default window settings */
	public static final String WINDOW_KEY = "window_data";
	/** The key of the default graphics settings */
	public static final String GRAPHICS_KEY = "graphics_default";
	
	/** Strings could be null or else this */
	public static final String NULL_MDO = "None";
	
	public static final List<Class<? extends MainSchema>> PRELOAD_SCHEMA;
	static {
		PRELOAD_SCHEMA = new ArrayList<Class< ? extends MainSchema>>();
		PRELOAD_SCHEMA.add(WindowSettingsMDO.class);
		PRELOAD_SCHEMA.add(GameSpeedMDO.class);
		PRELOAD_SCHEMA.add(UISettingsMDO.class);
	}
	
	private int rate;
	
	/**
	 * Reads the database for a bunch of constants.
	 */
	public Constants() {
		// load everything up~
		rate = RGlobal.data.getEntryFor("game_speed", GameSpeedMDO.class).framerate;
	}
	
	/** @return The target game framerate */
	public int rate() { return rate; }

}
