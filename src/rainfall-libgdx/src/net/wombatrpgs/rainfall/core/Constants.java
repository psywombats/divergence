/**
 *  Constants.java
 *  Created on Nov 25, 2012 12:39:01 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.core;

import net.wombatrpgs.rainfallschema.settings.GameSpeedMDO;

/**
 * Access to a bunch of magic numbers that are needed in a bunch of different
 * places. The idea is to only load up the schema once.
 */
public class Constants {
	
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
