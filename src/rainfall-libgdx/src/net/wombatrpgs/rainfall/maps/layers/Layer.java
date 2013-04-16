/**
 *  Layer.java
 *  Created on Nov 30, 2012 1:38:13 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.layers;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.FallResult;
import net.wombatrpgs.rainfall.physics.Hitbox;

/**
 * A layer in a map, either a grid layer or an object layer. It's how Tiled
 * handles it.
 */
public abstract class Layer implements Queueable {
	
	public static final String PROPERTY_Z = "z";
	
	/**
	 * Provides collision response service to an event on this layer.
	 * @param 	event			The event to push out of collision
	 */
	public abstract void applyPhysicalCorrections(MapEvent event);
	
	/**
	 * Gets the z-value of the layer. Layers with the same z-value share
	 * collisions and collision detection. 0 represents the floor, and each
	 * subsequent integer is another floor.
	 * @return					The z-value (depth) of this layer
	 */
	public abstract float getZ();
	
	/**
	 * Calculates what happens to a hitbox were it to be dropped on this layer.
	 * @param 	box				The hitbox being dropped
	 */
	public abstract FallResult dropObject(Hitbox box);

	/**
	 * Determines whether this layer is the floor, a so-called lower chip layer.
	 * This means that if there is no tile on this layer, that space will be
	 * impassable.
	 * @return					True if this layer is upper chip, else false
	 */
	public abstract boolean isLowerChip();
	
	/**
	 * Calculate the final passabilities of tiles in the layer. This should be
	 * called once all layers have been populated.
	 */
	public abstract void finalizePassability();
	
	/**
	 * Render yourself to the screen using OpenGL. This is different from the
	 * normal renderable method because it takes a z-parameter. Layers should
	 * only draw components of themselves that are on that specific z layer.
	 * @param	camera			The camera to render with
	 * @param	z				The z-layer to render components of
	 */
	public abstract void render(OrthographicCamera camera, float z);
	
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
