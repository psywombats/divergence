/**
 *  TestGenerator.java
 *  Created on Oct 4, 2013 2:27:45 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;

/**
 * The most basic generator!
 */
public class GeneratorTest extends MapGenerator {

	/**
	 * Generate a test generator from data.
	 * @param	mdo				The data to generate from
	 * @param	parent			The level to generate for
	 */
	public GeneratorTest(MapGeneratorMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.MapGenerator#generateInternal()
	 */
	@Override
	public void generateInternal() {
		int w = parent.getWidth();
		int h = parent.getHeight();
		TileType types[][] = new TileType[h][w];
		fillRect(types, TileType.ANY_CEILING, 0, 0, w-1, h-1);
		carve(types, TileType.FLOOR, 0,		0,		w-1,	0,		Halt.NONE);
		carve(types, TileType.FLOOR, w-1,	0,		w-1,	h-1,	Halt.NONE);
		carve(types, TileType.FLOOR, w-1,	h-1,	0,		h-1,	Halt.NONE);
		carve(types, TileType.FLOOR, 0,		h-1,	0,		0,		Halt.NONE);
		
		Tile tiles[][] = new Tile[h][w];
		applyWalls(types);
		convertTiles(types, null, tiles);
		addLayer(tiles, 0);
		
//		if (MGlobal.hero == null) {
//			MGlobal.hero = new Hero(parent, 0, 0);
//			assets.add(MGlobal.hero);
//			parent.addEvent(MGlobal.hero);
//		}
		
	}

}
