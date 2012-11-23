/**
 *  MapObject.java
 *  Created on Nov 12, 2012 11:10:52 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.graphics.Renderable;

/**
 * All objects that appear in Tiled maps that are not tiles extend this class.
 * This includes both characters and events.
 */
public abstract class MapObject implements Renderable, Positionable {
	
	protected Level parent;
	// x/y is in pixels, remember bro.
	protected int x, y;
	
	/**
	 * Creates a new map object for a given level and position.
	 * @param 	parent		The level this map object is on
	 * @param 	x			The x-coord in pixels of this object on the map
	 * @param 	y			The y-coord in pixels of this object on the map
	 */
	public MapObject(Level parent, int x, int y) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}

	/** @see net.wombatrpgs.rainfall.maps.Positionable#getX() */
	@Override
	public int getX() { return x; }

	/** @see net.wombatrpgs.rainfall.maps.Positionable#getY() */
	@Override
	public int getY() { return y; }

	/** @see net.wombatrpgs.rainfall.maps.Positionable#getBatch() */
	@Override
	public SpriteBatch getBatch() { return parent.getBatch(); }
	
	/**
	 * Renders a texture at this object's location using its own batch and
	 * coords appropriate to the drawing.
	 * @param 	sprite		The region to render
	 * @param	camera		The current camera
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite) {
		parent.getBatch().draw(sprite, x, parent.getHeightPixels() - y);
	}

}
