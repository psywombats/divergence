/**
 *  PosRenderable.java
 *  Created on Mar 5, 2014 11:22:26 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics.interfaces;

import net.wombatrpgs.mgne.core.interfaces.Queueable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Any implementers can be told where to render. Meant to unify the Graphic
 * family of UI things.
 */
public interface PosRenderable extends Queueable, Boundable {
	
	/**
	 * Renders itself at a specific location.
	 * @param 	batch			The batch to render the graphic as part of
	 * @param 	x				The x-coord to render at (in px)
	 * @param 	y				The y-coord to render at (in px)
	 */
	public void renderAt(SpriteBatch batch, float x, float y);

}
