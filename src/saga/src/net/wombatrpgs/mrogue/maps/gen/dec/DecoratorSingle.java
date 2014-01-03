/**
 *  DecoratorSingle.java
 *  Created on Oct 20, 2013 2:43:09 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen.dec;

import java.util.List;

import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.gen.MapGenerator;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;
import net.wombatrpgs.mrogueschema.maps.data.TileType;
import net.wombatrpgs.mrogueschema.maps.decorators.data.SingleDecoratorMDO;

/**
 * Yep that's right this thing relates to the MDO now. Cool!
 */
public abstract class DecoratorSingle extends Decorator {
	
	protected List<TileType> registered;
	protected SingleDecoratorMDO mdo;
	
	protected TileType original;
	protected TileType replace;
	protected Tile[][] tilesNew;

	/**
	 * Creates a new decorator from data.
	 * @param 	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 */
	public DecoratorSingle(SingleDecoratorMDO mdo, MapGenerator gen) {
		super(mdo, gen);
		original = mdo.original;
		replace = original;
		if (mdo.replacement != null) {
			replace = mdo.replacement;
		}
	}
	
	/**
	 * Checks if a certain square is legal according to our replacement scheme.
	 * Different from a straight lookup as it never creates inaccessible areas.
	 * Uses the default original/replace values.
	 * @param	tiles			The tiles to look up
	 * @param	x				The x-coord to look up, in tiles
	 * @param	y				The y-coord to look up, in tiles
	 * @return
	 */
	public boolean legal(Tile[][] tiles, int x, int y) {
		return legal(tiles, original, replace, x, y);
	}

	/**
	 * Checks if a certain square is legal according to our replacement scheme.
	 * Different from a straight lookup as it never creates inaccessible areas.
	 * @param	tiles			The tiles to look up
	 * @param	original		The original tile type
	 * @param	replace			The replacement tile type
	 * @param	x				The x-coord to look up, in tiles
	 * @param	y				The y-coord to look up, in tiles
	 * @return
	 */
	public boolean legal(Tile[][] tiles, TileType original, TileType replace, int x, int y) {
		if (x < 0 || x >= gen.getWidth() || y < 0 || y > gen.getHeight()) {
			return false;
		}
		if (tilesNew != tiles && tilesNew[y][x] != null) {
			return false;
		}
		if (!gen.isTile(tiles, original, x, y)) {
			return false;
		}
		if (original.isPassable() && !replace.isPassable()) {
			boolean flat[] = new boolean[10];
			int paths = 0;
			int at = 1;
			for (EightDir dir : EightDir.values()) {
				int offX = (int) dir.getVector().x;
				int offY = (int) dir.getVector().y;
				flat[at] = gen.isPassable(tiles, x + offX, y + offY) &&
						gen.isPassable(tilesNew, x + offX, y + offY);
				at += 1;
			}
			for (at = 1; at <= 9; at += 1) {
				if (!flat[at-1] && flat[at]) {
					paths += 1;
					for (; at <= 9 && flat[at]; at += 1);
				}
			}
			return paths <= 1;
		} else {
			return true;
		}
	}

}
