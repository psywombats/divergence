/**
 *  MapGenerator.java
 *  Created on Oct 4, 2013 2:23:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.TileType;
import net.wombatrpgs.mrogue.maps.layers.GridLayer;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;

/**
 * The thing that y'know generates maps. Each one is created with a specific
 * map in mind.
 */
public abstract class MapGenerator implements Queueable {
	
	protected MapGeneratorMDO mdo;
	protected Level parent;
	protected List<TileType> floorTiles;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new map generator from data.
	 * @param	mdo				The data to generate from
	 * @param	parent			The level to generate for
	 */
	public MapGenerator(MapGeneratorMDO mdo, Level parent) {
		this.mdo = mdo;
		this.parent = parent;
		this.assets = new ArrayList<Queueable>();
		this.floorTiles = new ArrayList<TileType>();
		for (String mdoKey : mdo.tiles) {
			floorTiles.add(MGlobal.tiles.getTile(mdoKey));
		}
	}
	
	/**
	 * Perform the actual construction of the level by bossing it around and
	 * making its layers.
	 */
	abstract public void generateMe();

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
		MGlobal.tiles.postProcessing(manager, pass);
	}
	
	/**
	 * Randomly selects a tile from a list.
	 * @param	tiles			The list of tiles to choose from
	 * @return					A random element from that list
	 */
	protected TileType getRandomTile(List<TileType> tiles) {
		return tiles.get(MGlobal.rand.nextInt(tiles.size()));
	}
	
	/**
	 * Adds a finished array of tiles to the map.
	 * @param	tiles			The tile data for the layer
	 * @param	z				The z-depth of the layer
	 */
	protected void addLayer(TileType[][] tiles, float z) {
		GridLayer layer = new GridLayer(parent, tiles, z);
		parent.addGridLayer(layer);
	}

}
