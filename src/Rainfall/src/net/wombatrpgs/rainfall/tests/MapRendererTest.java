/**
 *  MapRendererTest.java
 *  Created on Nov 8, 2012 10:22:30 PM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.tests;

import tiled.core.Map;
import tiled.io.TMXMapReader;
import tiled.view.OrthogonalRenderer;
import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.game.RainfallGame;
import net.wombatrpgs.rainfall.io.lwjgl.LWJGLFrontend;
import net.wombatrpgs.rainfallschema.MapLoadTestMDO;
import net.wombatrpgs.rainfallschema.WindowDataMDO;

/**
 * Loads up AND displays a map. Woahhhhh.
 */
public class MapRendererTest {
	
	/**
	 * @param 	args		Unused
	 */
	public static void main(String[] args) {
		
		Global.setupGlobalForTesting();
		Global.dataLoader.addToDatabase("res/data");
		
		MapLoadTestMDO mdo = (MapLoadTestMDO) Global.data.getEntryByKey("map_test");
		System.out.println("We're trying to load a " + mdo.map);
		TMXMapReader reader = new TMXMapReader();
		Map testMap = null;
		try {
			testMap = reader.readMap(Global.fileLoader.getStream("res/maps/" + mdo.map));
		} catch (Exception e) {
			Global.reporter.err("Some crazy shit happened getting the map!", e);
			e.printStackTrace();
		}
		Global.reporter.inform("We got ourselves a map!");
		Global.reporter.inform(testMap.toString());
		
		RainfallGame game = new RainfallGame();
		LWJGLFrontend front = new LWJGLFrontend(game);
		WindowDataMDO data = (WindowDataMDO) Global.data.getEntryByKey("window_data");
		front.start(data);
		
		OrthogonalRenderer renderer = new OrthogonalRenderer(testMap);

	}

}
