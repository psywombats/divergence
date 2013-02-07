/**
 *  MapObject.java
 *  Created on Nov 12, 2012 11:10:52 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.graphics.Renderable;

/**
 * All objects that appear in Tiled maps that are not tiles extend this class.
 * This includes both characters and events. As of 2012-01-23
 */
public abstract class MapObject implements	Renderable,
											Updateable {
	
	/** Level this object exists on */
	protected Level parent;
	/** All sub-objects that update with us */
	protected List<Updateable> subEvents;
	/** All sub-objects to remove later */
	protected List<Updateable> removalQueue;
	/** How we respond to pausing */
	protected PauseLevel pauseLevel;
	
	/**
	 * Creates a new map object for a given level.
	 * @param 	parent		The level this map object is on
	 */
	public MapObject(Level parent) {
		this();
		this.parent = parent;
	}
	
	/**
	 * Creates a new map object floating in limbo land.
	 */
	public MapObject() {
		subEvents = new ArrayList<Updateable>();
		removalQueue = new ArrayList<Updateable>();
		pauseLevel = PauseLevel.SURRENDERS_EASILY;
	}

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
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// default queues nothing
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// default does nothing
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		for (Updateable subEvent : subEvents) {
			subEvent.update(elapsed);
		}
		for (Updateable toRemove : removalQueue) {
			subEvents.remove(toRemove);
		}
	}
	
	/** @return The map we are currently on */
	public Level getLevel() { return parent; }
	
	/** @return How this object responds to pausing */
	public PauseLevel getPauseLevel() { return this.pauseLevel; }
	
	/** @param How this object will respond to pausing */
	public void setPauseLevel(PauseLevel level) { this.pauseLevel = level; }

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
	 * A hook system for sub-events. Adds another event that will be updated
	 * alongside this one.
	 * @param 	subEvent		The other object to update at the same time
	 */
	public void addSubEvent(Updateable subEvent) {
		subEvents.add(subEvent);
	}
	
	/**
	 * Remove the hook from a subevent. Stops it from updating.
	 * @param 	subEvent		The preregistered event to remove
	 */
	public void removeSubEvent(Updateable subEvent) {
		if (subEvents.contains(subEvent)) {
			removalQueue.add(subEvent);
		} else {
			RGlobal.reporter.warn("Removed a non-child subevent: " + subEvent);
		}
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
		int atX = (int) (x + Gdx.graphics.getWidth()/2 - camera.position.x);
		int atY = (int) (y + Gdx.graphics.getHeight()/2 - camera.position.y);
		Color c = parent.getBatch().getColor();
		float tint = (fallTime < .5f) ? 1-fallTime*2 : 0;
		parent.getBatch().setColor(tint*c.r, tint*c.b, tint*c.g, 1);
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

}
