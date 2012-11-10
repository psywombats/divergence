/**
 *  Launcher.java
 *  Created on Nov 8, 2012 6:57:31 PM for project Rainfall
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io.lwjgl;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.wombatrpgs.mgne.data.Database;
import net.wombatrpgs.mgne.data.DirectoryDataLoader;
import net.wombatrpgs.mgne.global.DebugReporter;
import net.wombatrpgs.mgne.global.FileLoader;
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
		
		initGlobal();
		Global.dataLoader.addToDatabase("res/data");
		
		RainfallGame game = new RainfallGame();
		LWJGLFrontend front = new LWJGLFrontend(game);
		WindowDataMDO data = (WindowDataMDO) Global.data.getEntryByKey("window_data");
		front.start(data);
	}
	
	/**
	 * Starts global up and running.
	 */
	public static void initGlobal() {
		Global.reporter = new DebugReporter();
		Global.fileLoader = new FileLoader();
		Global.dataLoader = new DirectoryDataLoader();
		Global.mapper = new ObjectMapper();
		Global.data = new Database();
	}

}
