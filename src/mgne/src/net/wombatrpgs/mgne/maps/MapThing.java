/**
 *  MapObject.java
 *  Created on Nov 12, 2012 11:10:52 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Renderable;

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
	 * Creates a new map object floating in limbo land.
	 */
	public MapThing() {
		pauseLevel = PauseLevel.SURRENDERS_EASILY;
		assets = new ArrayList<Queueable>();
	}
	
	/**
	 * Creates a new map object for a given level.
	 * @param 	parent		The level this map object is on
	 */
	public MapThing(Level parent) {
		this();
		this.parent = parent;
	}
	
	/** @return The map we are currently on */
	public Level getParent() { return parent; }
	
	/** @return How this object responds to pausing */
	public PauseLevel getPauseLevel() { return this.pauseLevel; }
	
	/** @param How this object will respond to pausing */
	public void setPauseLevel(PauseLevel level) { this.pauseLevel = level; }
	
	/**
	 * Default queues up everything in the assets list.
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
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
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// default does nothing
	}
	
	/**
	 * Defaults to the width of one tile.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth()
	 */
	@Override
	public int getWidth() {
		return parent.getTileWidth();
	}

	/**
	 * Defaults to the height of one tile.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight()
	 */
	@Override
	public int getHeight() {
		return parent.getTileHeight();
	}

	/**
	 * Default does nothing.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		// noop
	}

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
	 * Deteermines if a given string is the property key, ie, it is non-null and
	 * not the null key.
	 * @param 	property		The string from the MDO
	 * @return					True if it's a value, false it it's none
	 */
	public static boolean mdoHasProperty(String property) {
		return (property != null && !property.equals(Constants.NULL_MDO));
	}

}
