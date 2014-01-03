/**
 *  Layer.java
 *  Created on Nov 30, 2012 1:38:13 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.layers;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.maps.events.MapEvent;

/**
 * A layer in a map, either a grid layer or an object layer. It's how Tiled
 * handles it.
 */
public abstract class Layer implements Queueable {

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
	 * Checking to see if a position in the grid is passable. This is a little
	 * wonky for event layers...
	 * @param	actor			The event checking for collisions
	 * @param 	x				The x-coord of the tile to check (in tiles)
	 * @param 	y				The y-coord of the tile to check (in tiles)
	 * @return					True if that tile is passable, false otherwise
	 */
	public abstract boolean isPassable(MapEvent actor, final int x, final int y);
	
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
