/**
 *  Layer.java
 *  Created on Nov 30, 2012 1:38:13 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.layers;

import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.MapEvent;

/**
 * A layer in a map, either a grid layer or an object layer. It's how Tiled
 * handles it.
 */
public abstract class Layer implements Renderable {
	
	/**
	 * Provides collision response service to an event on this layer.
	 * @param 	event			The event to push out of collision
	 */
	public abstract void applyPhysicalCorrections(MapEvent event);

}
