/**
 *  Layer.java
 *  Created on Nov 30, 2012 1:38:13 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.maps.Level;

/**
 * A layer in a map, either a grid layer or an object layer. It's how Tiled
 * handles it.
 */
public abstract class Layer implements Queueable {
	
	protected Level parent;
	protected List<Queueable> assets;
	
	/**
	 * Creates a layer with a given parent map.
	 * @param	parent			The parent map of this layer
	 */
	public Layer(Level parent) {
		this.parent = parent;
		assets = new ArrayList<Queueable>();
	}
	
	/** Kryo constructor */
	protected Layer() { }

	/**
	 * Determines whether this layer is the floor, a so-called lower chip layer.
	 * This means that if there is no tile on this layer, that space will be
	 * impassable.
	 * @return					True if this layer is upper chip, else false
	 */
	public abstract boolean isLowerChip();
	
	/**
	 * Render yourself to the screen using OpenGL. This is different from the
	 * normal renderable method because it takes a z-parameter. Layers should
	 * only draw components of themselves that are on that specific z layer.
	 * MR: Just render, we'll call in right order
	 * @param	camera			The camera to render with
	 */
	public abstract void render(OrthographicCamera camera);
	
	/**
	 * Checking to see if a position in the grid is passable. I have no idea
	 * why this used to require an event.
	 * @param 	x				The x-coord of the tile to check (in tiles)
	 * @param 	y				The y-coord of the tile to check (in tiles)
	 * @return					True if that tile is passable, false otherwise
	 */
	public abstract boolean isTilePassable(int tileX, int tileY);
	
	/**
	 * Determines whether this layer is an object layer, a so-called upper
	 * chip layer. This means that unoccupied grid squares on this layer will
	 * be treated as passable.
	 * @return					True if this layer is lower chip, else false
	 */
	public boolean isUpperChip() {
		return !isLowerChip();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

}
