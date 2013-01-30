/**
 *  MapObject.java
 *  Created on Nov 12, 2012 11:10:52 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;

/**
 * All objects that appear in Tiled maps that are not tiles extend this class.
 * This includes both characters and events. As of 2012-01-23
 */
public abstract class MapObject implements	Renderable,
											Updateable {
	
	/** Level this object exists on */
	protected Level parent;
	
	/**
	 * Creates a new map object for a given level.
	 * @param 	parent		The level this map object is on
	 */
	protected MapObject(Level parent) {
		this();
		this.parent = parent;
	}
	
	/**
	 * Creates a new map object floating in limbo land.
	 */
	protected MapObject() {

	}
	
	/** @return The map the hero is currently on */
	public Level getLevel() { return parent; }

	/**
	 * This is actually the update part of the render loop. CHANGED on
	 * 2013-01-30 so that the level actually calls update separately. So don't
	 * worry about that.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		// default is invisible
	}
	
	/**
	 * Renders a texture at this object's location using its own batch and
	 * coords appropriate to the drawing. This is bascally a static method that
	 * could go in any Positionable but oh well.
	 * @param 	sprite			The region to render
	 * @param	camera			The current camera
	 * @param	x				The x-coord for rendering (in pixels)
	 * @param	y				The y-coord for rendering (in pixels)
	 */
	protected void renderLocal(OrthographicCamera camera, TextureRegion sprite, int x, int y) {
		parent.getBatch().draw(
				sprite, 
				x + Gdx.graphics.getWidth()/2 - camera.position.x, 
				y + Gdx.graphics.getHeight()/2 - camera.position.y);
	}
	
	/**
	 * Called when this object is added to an object layer. Nothing by default.
	 * @param 	layer			The layer this object is being added to
	 */
	public void onAdd(EventLayer layer) {
		// nothing by default
	}
	
	/**
	 * Called when this object is tele'd onto a map.
	 * @param 	map				The map this object is being removed from
	 */
	public void onTeleOn(Level map) {
		this.parent = map;
	}
	
	/**
	 * Called when this object is tele'd or otherwise removed from a map.
	 * @param 	map				The map this object is being removed from
	 */
	public void onTeleOff(Level map) {
		this.parent = null;
	}

}
