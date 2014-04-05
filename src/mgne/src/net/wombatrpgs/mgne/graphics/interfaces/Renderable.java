/**
 *  Renderable.java
 *  Created on Nov 11, 2012 2:56:40 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics.interfaces;

import net.wombatrpgs.mgne.core.interfaces.Queueable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Any object that can be written to a screen without further context.
 */
public interface Renderable extends Queueable, Boundable {
	
	/**
	 * Renders to the screen. The batch passed is probably either the viewbatch
	 * or the uibatch, but it shouldn't matter to implementations. This is to
	 * allow things like pictures that could be rendered in either context, and
	 * prevents weird offset things that might requires camera position.
	 * @param	batch			The batch to render with
	 */
	public void render(SpriteBatch batch);

}
