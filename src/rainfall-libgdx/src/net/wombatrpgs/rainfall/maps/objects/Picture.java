/**
 *  Picture.java
 *  Created on Feb 4, 2013 3:36:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.events.MapEvent;

/**
 * Replaces the old picture layer that the map had. This is exactly the RM
 * equivalent. The only difference it has with the map object is that it
 * can be compared against other pictures to sort by z-depth.
 */
public class Picture extends MapEvent {
	
	protected Graphic appearance;
	protected int z;
	
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
	 * Creates a picture with a certain appearance and depth.
	 * @param 	appearance		The graphic the picture will render
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int z) {
		this.z = z;
		this.appearance = appearance;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#compareTo
	 * (net.wombatrpgs.rainfall.maps.events.MapEvent)
	 */
	@Override
	public int compareTo(MapEvent other) {
		return -other.compareToPicture(this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#compareToPicture
	 * (net.wombatrpgs.rainfall.maps.objects.Picture)
	 */
	@Override
	public int compareToPicture(Picture pic) {
		return z - pic.z;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		appearance.renderAt(getBatch(), getX(), getY());
	}

}
