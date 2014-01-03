/**
 *  Queueable.java
 *  Created on Jan 22, 2013 5:28:52 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Any item that needs queuing to load its assets, and maybe some processing
 * when it's done.
 */
public interface Queueable {
	
	/**
	 * Queues up all the assets required to render this object in the resource
	 * manager. Does not actually load them. Called multiple times if there are
	 * still assets to load that were designated as needs-to-load by the post
	 * processing. This is only called once on any one object, whether by the
	 * main loading process or someone else's post process differes.
	 * @param	manager			The manager that will actually do the loading
	 */
	public void queueRequiredAssets(AssetManager manager);
	
	/**
	 * Perform all other necessary post-processing when the renderable assets
	 * are loaded. Called before the first render and after asset loading. Can
	 * queue assets if it so desires, in which case post processing will be
	 * called again by the loader. Rule of thumb: if you create/new something
	 * that requires post processing in a post processing, tell it to queue its
	 * assets.
	 * @param	manager			The manager that will actually do the loading
	 * @param	pass			The number of times the post processing has been
	 * 							called in this loading period
	 */
	public void postProcessing(AssetManager manager, int pass);

}
