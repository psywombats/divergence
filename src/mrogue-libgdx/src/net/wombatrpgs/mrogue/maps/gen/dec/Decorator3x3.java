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
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator3x3MDO;

/**
 * Generates a carpet thing.
 */
public class Decorator3x3 extends Decorator {
	
	protected Decorator3x3MDO mdo;

	/**
	 * Generates a decorator from data.
	 * @param	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 */
	public Decorator3x3(Decorator3x3MDO mdo, MapGenerator gen) {
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
				if (!gen.isType(tiles, gen.getTile(mdo.original), x+1, y+1)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x+1, y)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x+1, y-1)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x, y-1)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x, y)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x, y+1)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x-1, y+1)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x-1, y)) continue;
				if (!gen.isType(tiles, gen.getTile(mdo.original), x-1, y-1)) continue;
				tiles[y+1][x-1] = MGlobal.tiles.getTile(mdo.ul);
				tiles[y+1][x] = MGlobal.tiles.getTile(mdo.u);
				tiles[y+1][x+1] = MGlobal.tiles.getTile(mdo.ur);
				tiles[y][x-1] = MGlobal.tiles.getTile(mdo.l);
				tiles[y][x] = MGlobal.tiles.getTile(mdo.c);
				tiles[y][x+1] = MGlobal.tiles.getTile(mdo.r);
				tiles[y-1][x-1] = MGlobal.tiles.getTile(mdo.bl);
				tiles[y-1][x] = MGlobal.tiles.getTile(mdo.b);
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
		MGlobal.tiles.requestTile(manager, mdo.ul, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.u, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.ur, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.l, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.c, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.r, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.bl, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.b, mdo.original);
		MGlobal.tiles.requestTile(manager, mdo.br, mdo.original);
	}

}
