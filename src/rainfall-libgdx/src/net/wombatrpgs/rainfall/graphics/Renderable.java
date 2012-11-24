/**
 *  Renderable.java
 *  Created on Nov 11, 2012 2:56:40 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * All objects that are to be drawn to the screen must implement this. It just
 * contains a basic render method... for now.
 */
public interface Renderable {
	
	/**
	 * Render yourself to the screen using OpenGL. Nothing needs to be passed
	 * here because OpenGL is one giant statemachine clusterfuck.
	 * @param	camera		The camera to render with, may or may not be used
	 */
	public void render(OrthographicCamera camera);
	
	/**
	 * Queues up all the assets required to render this object in the resource
	 * manager. Does not actually load them.
	 */
	public void queueRequiredAssets(AssetManager manager);
	
	/**
	 * Perform all other necessary post-processing when the renderable assets
	 * are loaded. Called before the first render and after asset loading.
	 */
	public void postProcessing();

}
