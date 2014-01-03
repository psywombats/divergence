/**
 *  Decorator.java
 *  Created on Oct 13, 2013 6:25:51 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen.dec;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.gen.MapGenerator;
import net.wombatrpgs.mrogueschema.maps.decorators.data.DecoratorMDO;

/**
 * Makes maps look pretty once they're done.
 */
public abstract class Decorator implements Queueable {
	
	protected DecoratorMDO mdo;
	protected MapGenerator gen;
	
	/**
	 * Creates a new decorator from data.
	 * @param	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 */
	public Decorator(DecoratorMDO mdo, MapGenerator gen) {
		this.mdo = mdo;
		this.gen = gen;
	}
	
	/**
	 * Adds the decoration tiles to the scene.
	 * @param	tilesOld		The set of tiles to refer for overwrite type
	 * @param	tilesNew		The set of tiles to mutate
	 */
	public abstract void apply(Tile[][] tilesOld, Tile[][] tilesNew);

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// default is nothing
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// Default is nothing
	}

}
