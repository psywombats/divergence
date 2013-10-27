/**
 *  MapObject.java
 *  Created on Nov 12, 2012 11:10:52 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.graphics.Renderable;

/**
 * All objects that appear in Tiled maps that are not tiles extend this class.
 * This includes both characters and events. As of 2012-01-23 it's called Thing
 * and not Object to prevent a name collision with some LibGDX class.
 */
public abstract class MapThing implements	Renderable,
											Updateable {
	
	/** Level this object exists on */
	protected Level parent;
	/** How we respond to pausing */
	protected PauseLevel pauseLevel;
	/** Things that need to be loaded in the queue/post phases */
	protected List<Queueable> assets;
	
	/**
	 * Creates a new map object for a given level.
	 * @param 	parent		The level this map object is on
	 */
	public MapThing(Level parent) {
		this();
		this.parent = parent;
	}
	
	/**
	 * Creates a new map object floating in limbo land.
	 */
	public MapThing() {
		pauseLevel = PauseLevel.SURRENDERS_EASILY;
		assets = new ArrayList<Queueable>();
	}

	/**
	 * This is actually the update part of the render loop. CHANGED on
	 * 2013-01-30 so that the level actually calls update separately. So don't
	 * worry about that.
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		// default is invisible
	}
	
	/**
	 * Default queues up everything in the assets list.
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * Default processes everything in the assets list.
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// default does nothing
	}
	
	/** @return The map we are currently on */
	public Level getParent() { return parent; }
	
	/** @return How this object responds to pausing */
	public PauseLevel getPauseLevel() { return this.pauseLevel; }
	
	/** @param How this object will respond to pausing */
	public void setPauseLevel(PauseLevel level) { this.pauseLevel = level; }
	
	/**
	 * Called when the level resets. Return to the default position. This should
	 * involve either reseting coords to where they were when the level was
	 * generated or removing self from the map. The default removes self from
	 * map; chances are we shouldn't be there.
	 */
	public void reset() {
		parent.removeObject(this);
	}
	
	/**
	 * Called when the parent map loses focus due to the hero teleporting to
	 * someplace else. Default does nothing.
	 * @param 	map				The map that lost focus, should be our parent
	 */
	public void onMapFocusLost(Level map) {
		// default is nothing.
	}

	/**
	 * Called when this object is tele'd onto a map.
	 * @param 	map				The map this object is being removed from
	 */
	public void onAddedToMap(Level map) {
		this.parent = map;
	}
	
	/**
	 * Called when this object is tele'd or otherwise removed from a map.
	 * @param 	map				The map this object is being removed from
	 */
	public void onRemovedFromMap(Level map) {
		this.parent = null;
	}
	
	/**
	 * Renders a texture at this object's location using its own batch and
	 * coords appropriate to the drawing. This is bascally a static method that
	 * could go in any Positionable but oh well.
	 * @param 	sprite			The region to render
	 * @param	camera			The current camera
	 * @param	x				The x-coord for rendering (in pixels)
	 * @param	y				The y-coord for rendering (in pixels)
	 * @param	fallTime		How far fallen we are (0-1)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite, 
			int x, int y, float fallTime) {
		renderLocal(camera, sprite, x, y, 0, fallTime);
	}
	
	/**
	 * Renders a texture at this object's location using its own batch and
	 * coords appropriate to the drawing. This one takes rotation into account.
	 * @param 	sprite			The region to render
	 * @param	camera			The current camera
	 * @param	x				The x-coord for rendering (in pixels)
	 * @param	y				The y-coord for rendering (in pixels)
	 * @param	angle			The angle to render at
	 * @param	fallTime		How far fallen we are (0-1)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite, 
			int x, int y, int angle, float fallTime) {
		if (parent == null) return;
		float atX = x;
		float atY = y;
		Color c = parent.getBatch().getColor().cpy();
		float tint = (fallTime < .5f) ? 1-fallTime*2 : 0;
		parent.getBatch().setColor(tint*c.r, tint*c.b, tint*c.g, tint*c.a);
		parent.getBatch().draw(
				sprite,
				atX, 
				atY,
				sprite.getRegionWidth() / 2,
				sprite.getRegionHeight() / 2, 
				sprite.getRegionWidth(),
				sprite.getRegionHeight(), 
				1f-fallTime,
				1f-fallTime, 
				angle);
		parent.getBatch().setColor(c);
	}
	
	/**
	 * Deteermines if a given string is the property key, ie, it is non-null and
	 * not the null key.
	 * @param 	property		The string from the MDO
	 * @return					True if it's a value, false it it's none
	 */
	public static boolean mdoHasProperty(String property) {
		return (property != null && !property.equals(Constants.NULL_MDO));
	}

}
