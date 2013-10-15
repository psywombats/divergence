/**
 *  Decorator3x3.java
 *  Created on Oct 13, 2013 6:28:32 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen.dec;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.gen.MapGenerator;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator2x2MDO;

/**
 * Generates a painting thing.
 */
public class Decorator2x2 extends Decorator {
	
	protected Decorator2x2MDO mdo;

	/**
	 * Generates a decorator from data.
	 * @param	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 */
	public Decorator2x2(Decorator2x2MDO mdo, MapGenerator gen) {
		super(mdo, gen);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.dec.Decorator#apply
	 * (net.wombatrpgs.mrogue.maps.Tile[][])
	 */
	@Override
	public void apply(Tile[][] tiles) {
		for (int x = 0; x < gen.getWidth(); x += 1) {
			for (int y = 0; y < gen.getHeight(); y += 1) {
				if (mdo.chance < gen.rand().nextFloat()) continue;
				if (!gen.isTile(tiles, mdo.original, x+1, y-1)) continue;
				if (!gen.isTile(tiles, mdo.original, x+1, y)) continue;
				if (!gen.isTile(tiles, mdo.original, x, y-1)) continue;
				if (!gen.isTile(tiles, mdo.original, x, y)) continue;
				tiles[y][x] = MGlobal.tiles.getTile(mdo.tl);
				tiles[y][x+1] = MGlobal.tiles.getTile(mdo.tr);
				tiles[y-1][x] = MGlobal.tiles.getTile(mdo.bl);
				tiles[y-1][x+1] = MGlobal.tiles.getTile(mdo.br);
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.dec.Decorator#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		MGlobal.tiles.requestTile(manager, mdo.tl, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.tr, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.bl, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.br, mdo.original);
	}

}
