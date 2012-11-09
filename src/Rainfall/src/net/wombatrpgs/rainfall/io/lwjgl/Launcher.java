/**
 *  Launcher.java
 *  Created on Nov 8, 2012 6:57:31 PM for project Rainfall
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io.lwjgl;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.game.RainfallGame;
import net.wombatrpgs.rainfallschema.WindowDataMDO;

/**
 * Launches Rainfall in LWJGL mode.
 */
public class Launcher {
	
	/**
	 * Starts the game!
	 * @param 	args		Unused... for now
	 */
	public static void main(String[] args) {
		
		Global.dataLoader.addToDatabase("res/data");
		
		RainfallGame game = new RainfallGame();
		LWJGLFrontend front = new LWJGLFrontend(game);
		WindowDataMDO data = (WindowDataMDO) Global.data.getEntryByKey("window_data");
		front.start(data);
	}

}
