/**
 *  Picture.java
 *  Created on Feb 4, 2013 3:36:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.Graphic;

/**
 * Replaces the old picture layer that the map had. This is exactly the RM
 * equivalent. The only difference it has with the map object is that it
 * can be compared against other pictures to sort by z-depth.
 * 
 * Don't use it to display subcomponents!
 */
public class Picture extends ScreenGraphic implements Comparable<Picture> {
	
	protected Graphic appearance;
	protected int z;
	
	/**
	 * Create a picture at an explicit location
	 * @param 	appearance		The graphic the picture will render
	 * @param 	x				The x-coord (in pixels) to render at
	 * @param 	y				The y-coord (in pixels) to render at	
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, float x, float y, int z) {
		super(x, y);
		this.z = z;
	}
	
	/**
	 * Creates a picture with a certain appearance and depth at 0, 0.
	 * @param 	appearance		The graphic the picture will render
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int z) {
		this(appearance, 0, 0, z);
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic).
	 * @param 	fileName		The name of the file with the graphic (in UI)
	 * @param 	x				The x-coord (in pixels) to render at
	 * @param 	y				The y-coord (in pixels) to render at
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(String fileName, float x, float y, int z) {
		this(new Graphic(fileName), x, y, z);
		assets.add(appearance);
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic) at 0,0.
	 * @param 	fileName		The name of the file (in UI)
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(String fileName, int z) {
		this(fileName, 0, 0, z);
	}
	
	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getWidth() */
	@Override public int getWidth() { return appearance.getWidth(); }

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getHeight() */
	@Override public int getHeight() { return appearance.getHeight(); }

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		appearance.renderAt(batch, x, y);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Picture o) {
		return z - o.z;
	}

}
