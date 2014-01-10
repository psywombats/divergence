/**
 *  Decorator3x3.java
 *  Created on Oct 13, 2013 6:28:32 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps.gen.dec;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.maps.Tile;
import net.wombatrpgs.saga.maps.gen.MapGenerator;
import net.wombatrpgs.sagaschema.maps.decorators.Decorator3x3MDO;

/**
 * Generates a carpet thing.
 */
public class Decorator3x3 extends DecoratorSingle {
	
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
	 * @see net.wombatrpgs.saga.maps.gen.dec.Decorator#apply
	 * (net.wombatrpgs.saga.maps.Tile[][])
	 */
	@Override
	public void apply(Tile[][] tilesOld, Tile[][] tilesNew) {
		this.tilesNew = tilesNew;
		this.tilesNew = tilesNew;
		for (int x = 0; x < gen.getWidth(); x += 1) {
			for (int y = 0; y < gen.getHeight(); y += 1) {
				if (mdo.chance < gen.rand().nextFloat()) continue;
				if (!legal(tilesOld, x+1, y+1)) continue;
				if (!legal(tilesOld, x+1, y)) continue;
				if (!legal(tilesOld, x+1, y-1)) continue;
				if (!legal(tilesOld, x, y-1)) continue;
				if (!legal(tilesOld, x, y)) continue;
				if (!legal(tilesOld, x, y+1)) continue;
				if (!legal(tilesOld, x-1, y+1)) continue;
				if (!legal(tilesOld, x-1, y)) continue;
				if (!legal(tilesOld, x-1, y-1)) continue;
				tilesNew[y+1][x-1] = SGlobal.tiles.getTile(mdo.ul);
				tilesNew[y+1][x] = SGlobal.tiles.getTile(mdo.u);
				tilesNew[y+1][x+1] = SGlobal.tiles.getTile(mdo.ur);
				tilesNew[y][x-1] = SGlobal.tiles.getTile(mdo.l);
				tilesNew[y][x] = SGlobal.tiles.getTile(mdo.c);
				tilesNew[y][x+1] = SGlobal.tiles.getTile(mdo.r);
				tilesNew[y-1][x-1] = SGlobal.tiles.getTile(mdo.bl);
				tilesNew[y-1][x] = SGlobal.tiles.getTile(mdo.b);
				tilesNew[y-1][x+1] = SGlobal.tiles.getTile(mdo.br);
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.saga.maps.gen.dec.Decorator#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		SGlobal.tiles.requestTile(manager, mdo.ul, replace);
		SGlobal.tiles.requestTile(manager, mdo.u, replace);
		SGlobal.tiles.requestTile(manager, mdo.ur, replace);
		SGlobal.tiles.requestTile(manager, mdo.l, replace);
		SGlobal.tiles.requestTile(manager, mdo.c, replace);
		SGlobal.tiles.requestTile(manager, mdo.r, replace);
		SGlobal.tiles.requestTile(manager, mdo.bl, replace);
		SGlobal.tiles.requestTile(manager, mdo.b, replace);
		SGlobal.tiles.requestTile(manager, mdo.br, replace);
	}

}
