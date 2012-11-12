/**
 *  Renderable.java
 *  Created on Nov 11, 2012 2:56:40 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

/**
 * All objects that are to be drawn to the screen must implement this. It just
 * contains a basic render method... for now.
 */
public interface Renderable {
	
	/**
	 * Render yourself to the screen using OpenGL. Nothing needs to be passed
	 * here because OpenGL is one giant statemachine clusterfuck.
	 */
	public void render();

}
