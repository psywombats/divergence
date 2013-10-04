/**
 *  Renderable.java
 *  Created on Nov 11, 2012 2:56:40 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import net.wombatrpgs.mrogue.core.Queueable;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * All objects that are to be drawn to the screen must implement this. It just
 * contains a basic render method. However, due to how libgdx sets up its
 * interal loops, this should be thought of as more of an "updateable." Any
 * updating should be done in the render loop by taking elapsed time to render
 * frame into account.
 */
public interface Renderable extends Queueable {
	
	/**
	 * Render yourself to the screen using OpenGL. Nothing needs to be passed
	 * here because OpenGL is one giant statemachine clusterfuck. Keep in mind
	 * due to libgdx weirdness, updates should be done in here as well.
	 * @param	camera		The camera to render with, may or may not be used
	 */
	public void render(OrthographicCamera camera);

}
