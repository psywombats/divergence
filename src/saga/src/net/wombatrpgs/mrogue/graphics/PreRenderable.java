/**
 *  PreRenderable.java
 *  Created on Feb 7, 2013 9:54:44 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Can produce a texture and a place to render it. This replaces the old render
 * bump system.
 */
public interface PreRenderable {
	
	/**
	 * The x-coord (in px) of where to render this texture. This is the lower
	 * left corner and relative to the bottom right of the screen.
	 * @return					The x-coord (in px) to render at
	 */
	public int getRenderX();
	
	/**
	 * The y-coord (in px) of where to render this texture. This is the lower
	 * left corner and relative to the bottom right of the screen.
	 * @return					The y-coord (in px) to render at
	 */
	public int getRenderY();
	
	/**
	 * The texture to render. At some point this will be split into multiple
	 * parts, all split into chunks with y-values equal to the tiled map's tile
	 * height. The logic here is that these values are interlaced with tiled
	 * layers so as to prevent excessively tall objects from appearing weirdly
	 * when next to layers of greater z-value.
	 * @return					All texture regions to render, low y first
	 */
	public TextureRegion getRegion();

}
