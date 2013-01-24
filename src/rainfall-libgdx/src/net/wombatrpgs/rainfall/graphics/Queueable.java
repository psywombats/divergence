/**
 *  Queueable.java
 *  Created on Jan 22, 2013 5:28:52 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Any item that needs queuing to load its assets, and maybe some processing
 * when it's done.
 */
public interface Queueable {
	
	/**
	 * Queues up all the assets required to render this object in the resource
	 * manager. Does not actually load them.
	 */
	public void queueRequiredAssets(AssetManager manager);
	
	/**
	 * Perform all other necessary post-processing when the renderable assets
	 * are loaded. Called before the first render and after asset loading.
	 */
	public void postProcessing(AssetManager manager);

}
