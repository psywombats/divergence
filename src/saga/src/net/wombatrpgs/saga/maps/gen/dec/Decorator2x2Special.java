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
import net.wombatrpgs.sagaschema.maps.decorators.Decorator2x2SpecialMDO;
import net.wombatrpgs.sagaschema.maps.decorators.data.ShelfMode;

/**
 * Generates a 2x2 with specific-er tile requirements.
 */
public class Decorator2x2Special extends DecoratorSingle {
	
	protected Decorator2x2SpecialMDO mdo;

	/**
	 * Generates a decorator from data.
	 * @param	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 */
	public Decorator2x2Special(Decorator2x2SpecialMDO mdo, MapGenerator gen) {
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
		for (int x = 0; x < gen.getWidth(); x += 1) {
			for (int y = 0; y < gen.getHeight(); y += 1) {
				if (mdo.chance < gen.rand().nextFloat()) continue;
				if (mdo.shelfMode == ShelfMode.NOT_SHELF) {
					if (!gen.isTile(tilesOld, mdo.blOriginal, x+1, y-1)) continue;
					if (!gen.isTile(tilesOld, mdo.brOriginal, x, y-1)) continue;
				} else {
					if (!legal(tilesOld, mdo.blOriginal, mdo.replacement, x+1, y-1)) continue;
					if (!legal(tilesOld, mdo.blOriginal, mdo.replacement, x, y-1)) continue;
				}
				if (!gen.isTile(tilesOld, mdo.trOriginal, x+1, y)) continue;
				if (!gen.isTile(tilesOld, mdo.tlOriginal, x, y)) continue;
				tilesNew[y][x] = SGlobal.tiles.getTile(mdo.tl);
				tilesNew[y][x+1] = SGlobal.tiles.getTile(mdo.tr);
				tilesNew[y-1][x] = SGlobal.tiles.getTile(mdo.bl);
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
		SGlobal.tiles.requestTile(manager, mdo.tl, mdo.tlOriginal);
		SGlobal.tiles.requestTile(manager, mdo.tr, mdo.trOriginal);
		if (mdo.shelfMode == ShelfMode.NOT_SHELF) {
			SGlobal.tiles.requestTile(manager, mdo.bl, mdo.blOriginal);
			SGlobal.tiles.requestTile(manager, mdo.br, mdo.brOriginal);
		} else {
			SGlobal.tiles.requestTile(manager, mdo.bl, mdo.replacement);
			SGlobal.tiles.requestTile(manager, mdo.br, mdo.replacement);
		}
	}

}
