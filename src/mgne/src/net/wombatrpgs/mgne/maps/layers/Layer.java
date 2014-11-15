/**
 *  Layer.java
 *  Created on Nov 30, 2012 1:38:13 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.graphics.interfaces.Renderable;
import net.wombatrpgs.mgne.maps.Level;

/**
 * A layer in a map, either a grid layer or an object layer. It's how Tiled
 * handles it.
 */
public abstract class Layer extends AssetQueuer implements	Renderable,
															Disposable {
	
	protected Level parent;
	
	/**
	 * Creates a layer with a given parent map.
	 * @param	parent			The parent map of this layer
	 */
	public Layer(Level parent) {
		super();
		this.parent = parent;
	}

	/**
	 * Determines whether this layer is the floor, a so-called lower chip layer.
	 * This means that if there is no tile on this layer, that space will be
	 * impassable.
	 * @return					True if this layer is upper chip, else false
	 */
	public abstract boolean isLowerChip();
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return parent.getWidth(); }

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return parent.getHeight(); }
	
	/**
	 * Checking to see if a position in the grid is passable. I have no idea
	 * why this used to require an event.
	 * @param 	tileX			The x-coord of the tile to check (in tiles)
	 * @param 	tileY			The y-coord of the tile to check (in tiles)
	 * @return					True if that tile is passable, false otherwise
	 */
	public abstract boolean isTilePassable(int tileX, int tileY);
	
	/**
	 * Checking to see if a position in the grid has any tile in it.
	 * @param 	tileX			The x-coord of the tile to check (in tiles)
	 * @param 	tileY			The y-coord of the tile to check (in tiles)
	 * @return					True if any tile exists there
	 */
	public abstract boolean hasTileAt(int tileX, int tileY);

	/**
	 * Determines whether this layer is an object layer, a so-called upper
	 * chip layer. This means that unoccupied grid squares on this layer will
	 * be treated as passable.
	 * @return					True if this layer is lower chip, else false
	 */
	public boolean isUpperChip() {
		return !isLowerChip();
	}

}
