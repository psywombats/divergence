/**
 *  TMXReaderTest.java
 *  Created on Nov 4, 2012 5:41:37 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.tests;

import net.wombatrpgs.mgne.global.Global;

import tiled.core.Map;
import tiled.io.TMXMapReader;

/**
 * A class for reading in map files. Not a unit test, don't get any funny ideas.
 */
public class TMXReaderTest {
	
	public static void main(String args[]) {
		Global.setupGlobalForTesting();
		TMXMapReader reader = new TMXMapReader();
		Map testMap = null;
		try {
			testMap = reader.readMap(Global.fileLoader.getStream("res/test/blockbound.tmx"));
		} catch (Exception e) {
			Global.reporter.err("Some crazy shit happened getting the map!", e);
			e.printStackTrace();
		}
		Global.reporter.inform("We got ourselves a map!");
		Global.reporter.inform(testMap.toString());
	}

}
