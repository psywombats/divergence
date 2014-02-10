/**
 *  Picture.java
 *  Created on Feb 4, 2013 3:36:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.graphics.Graphic;
import net.wombatrpgs.mgne.graphics.ScreenDrawable;

/**
 * Replaces the old picture layer that the map had. This is exactly the RM
 * equivalent. The only difference it has with the map object is that it
 * can be compared against other pictures to sort by z-depth.
 */
public class Picture extends ScreenDrawable {
	
	protected Graphic appearance;
	
	/**
	 * Create a picture at an explicit location
	 * @param 	appearance		The graphic the picture will render
	 * @param 	x				The x-coord (in pixels) to render at
	 * @param 	y				The y-coord (in pixels) to render at	
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int x, int y, int z) {
		this(appearance, z);
		setX(x);
		setY(y);
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic).
	 * @param 	fileName		The name of the file with the graphic (in UI)
	 * @param 	x				The x-coord (in pixels) to render at
	 * @param 	y				The y-coord (in pixels) to render at
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(String fileName, int x, int y, int z) {
		this(new Graphic(fileName), z);
		this.assets.add(appearance);
		setX(x);
		setY(y);
	}
	
	/**
	 * Creates a picture with a certain appearance and depth.
	 * @param 	appearance		The graphic the picture will render
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int z) {
		super(z);
		this.appearance = appearance;
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic) at 0,0.
	 * @param 	fileName		The name of the file (in UI)
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(String fileName, int z) {
		this(fileName, 0, 0, z);
	}
	
	/** @return The width of the underlying graphic, in px */
	public int getWidth() { return appearance.getWidth(); }
	
	/** @return The height of the underlying graphic, in px */
	public int getHeight() { return appearance.getHeight(); }

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		batch.setColor(currentColor);
		appearance.renderAt(batch, x, y);
	}

}
