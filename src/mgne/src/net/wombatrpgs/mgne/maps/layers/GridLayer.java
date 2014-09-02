/**
 *  GridLayer.java
 *  Created on Jan 8, 2014 4:06:26 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import net.wombatrpgs.mgne.maps.Level;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really. This is split into two halves, a generated grid layer and a
 * loaded grid layer. Generated grid layers run off a tile grid, and the loaded
 * ones just make reference to the tiled layer that spawned them. This class
 * holds their common functionality.
 */
public abstract class GridLayer extends Layer {
	
	protected float z;
	
	/**
	 * Creates a new grid layer of any type.
	 * @param	parent			The parent level of the layer
	 * @param	z				The z-float of this layer, follows some weird
	 * 							fractional for upper chip, whole for lower chip
	 * 							rules that should really be explained somewhere	
	 */
	public GridLayer(Level parent, float z) {
		super(parent);
		this.z = z;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		return Math.floor(z) == z;
	}
	
	/**
	 * Checks if the tile at the given location bridges over lower chips.
	 * @param	tileX			The x-coord to check (in tiles)
	 * @param	tileY			The y-coord to check (in tiles)
	 * @return					True if the tile at location is bridging
	 */
	public abstract boolean isBridge(int tileX, int tileY);
	
	/**
	 * Gets the z-value of the layer. Layers with the same z-value share
	 * collisions and collision detection. 0 represents the floor, and each
	 * subsequent integer is another floor.
	 * @return					The z-value (depth) of this layer
	 */
	public float getZ() {
		return z;
	}

}
