/**
 *  TestGenerator.java
 *  Created on Oct 4, 2013 2:27:45 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import net.wombatrpgs.mrogue.characters.Hero;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.TileType;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;

/**
 * The most basic generator!
 */
public class TestGenerator extends MapGenerator {

	/**
	 * Generate a test generator from data.
	 * @param	mdo				The data to generate from
	 * @param	parent			The level to generate for
	 */
	public TestGenerator(MapGeneratorMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.MapGenerator#generateMe()
	 */
	@Override
	public void generateMe() {
		TileType[][] tiles = new TileType[parent.getHeight()][parent.getWidth()];
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
//				if (y == 0) {
					tiles[y][x] = getRandomTile(floorTiles);
//				} else {
//					tiles[y][x] = floorTiles.get(0);
//				}
			}
		}
		addLayer(tiles, 0);
		
		if (MGlobal.hero == null) {
			MGlobal.hero = new Hero(parent, 0, 0);
			assets.add(MGlobal.hero);
			parent.addEvent(MGlobal.hero);
		}
	}

}
