/**
 *  PosRenderable.java
 *  Created on Mar 5, 2014 11:22:26 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Any implementers can be told where to render. Meant to unify the Graphic
 * family of UI things.
 */
public interface PosRenderable {
	
	/**
	 * Calculates or retrieves the width of the renderable represented.
	 * @return					The width of the resulting image (in real px)
	 */
	public int getWidth();
	
	/**
	 * Calculates or retrieves the height of the renderable represented.
	 * @return					The height of the resulting image (in real px)
	 */
	public int getHeight();
	
	/**
	 * Renders itself at a specific location.
	 * @param 	batch			The batch to render the graphic as part of
	 * @param 	x				The x-coord to render at (in px)
	 * @param 	y				The y-coord to render at (in px)
	 */
	public void renderAt(SpriteBatch batch, float x, float y);
	
	/**
	 * Renders itself at a specific location.
	 * @param 	batch			The batch to render the graphic as part of
	 * @param 	x				The x-coord to render at (in px)
	 * @param 	y				The y-coord to render at (in px)
	 * @param	scaleX			The scale for width, 1 is standard
	 * @param	scaleY			The scale for height, 1 is standard
	 */
	public void renderAt(SpriteBatch batch, float x, float y, float scaleX, float scaleY);

}
