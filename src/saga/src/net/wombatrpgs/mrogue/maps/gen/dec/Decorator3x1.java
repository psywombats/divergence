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
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator3x1MDO;

/**
 * Generates a table thing.
 */
public class Decorator3x1 extends DecoratorSingle {
	
	protected Decorator3x1MDO mdo;

	/**
	 * Generates a decorator from data.
	 * @param	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 */
	public Decorator3x1(Decorator3x1MDO mdo, MapGenerator gen) {
		super(mdo, gen);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.dec.Decorator#apply
	 * (net.wombatrpgs.mrogue.maps.Tile[][])
	 */
	@Override
	public void apply(Tile[][] tilesOld, Tile[][] tilesNew) {
		this.tilesNew = tilesNew;
		this.tilesNew = tilesNew;
		for (int x = 0; x < gen.getWidth(); x += 1) {
			for (int y = 0; y < gen.getHeight(); y += 1) {
				if (mdo.chance < gen.rand().nextFloat()) continue;
				if (!legal(tilesOld, x-1, y)) continue;
				if (!legal(tilesOld, x, y)) continue;
				if (!legal(tilesOld, x+1, y)) continue;
				tilesNew[y][x] = MGlobal.tiles.getTile(mdo.m);
				tilesNew[y][x+1] = MGlobal.tiles.getTile(mdo.r);
				tilesNew[y][x-1] = MGlobal.tiles.getTile(mdo.l);
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
		MGlobal.tiles.requestTile(manager, mdo.l, replace);
		MGlobal.tiles.requestTile(manager, mdo.m, replace);
		MGlobal.tiles.requestTile(manager, mdo.r, replace);
	}

}
