/**
 *  DecoratorSet.java
 *  Created on Oct 17, 2013 2:50:09 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen.dec;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.gen.MapGenerator;
import net.wombatrpgs.mrogueschema.maps.decorators.DecoratorSetMDO;
/**
 * A bunch of decorators applied back to back.
 */
public class DecoratorSet extends Decorator {
	
	protected DecoratorSetMDO mdo;
	protected List<Decorator> children;

	/**
	 * Constructs a new decorator set from data.
	 * @param	mdo				The data to generate from
	 * @param	gen				The map generator to generate for
	 */
	public DecoratorSet(DecoratorSetMDO mdo, MapGenerator gen) {
		super(mdo, gen);
		this.mdo = mdo;
		children = new ArrayList<Decorator>();
		for (String key : mdo.decorators) {
			children.add(DecoratorFactory.createDecor(key, gen));
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.dec.Decorator#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		for (Decorator child : children) {
			child.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.dec.Decorator#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		for (Decorator child : children) {
			child.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.gen.dec.Decorator#apply
	 * (net.wombatrpgs.mrogue.maps.Tile[][])
	 */
	@Override
	public void apply(Tile[][] tilesOld, Tile[][] tilesNew){
		for (Decorator child : children) {
			child.apply(tilesOld, tilesNew);
		}
	}

}
