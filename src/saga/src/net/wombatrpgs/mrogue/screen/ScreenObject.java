/**
 *  Canvasable.java
 *  Created on Jan 30, 2013 1:00:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.graphics.Renderable;

/**
 * Something that's both renderable and updateable. Can also be sorted.
 */
public abstract class ScreenObject implements	Renderable, 
												Updateable,
												Comparable<ScreenObject> {
	
	protected List<Queueable> assets;
	
	protected int z;
	
	/**
	 * Creates a blank screen object.
	 */
	public ScreenObject() {
		this.assets = new ArrayList<Queueable>();
	}
	
	/**
	 * Creates a blank screen object.
	 * @param	z				The z-layer of this object
	 */
	public ScreenObject(int z) {
		this();
		this.z = z;
	}
	
	/** @return The z-layer of this object */
	public int getZ() { return z; }

	/**
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
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * 		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}(com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ScreenObject o) {
		return z - o.z;
	}

	/**
	 * Default is nothing.
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// noop
	}

	/**
	 * Default is nothing.
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		// noop
	}
	
	/**
	 * If this is true, ignores the screen's transition tint. This means that
	 * it will have to be rendered in a separate phase.
	 * @return					True if tint is ignored, false if it is applied
	 */
	public boolean ignoresTint() {
		return false;
	}
	
	/**
	 * Called when this object is added to the screen. Default is nothing.
	 */
	public void onAddedToScreen() {
		// noop
	}
	
	/**
	 * Called when this object is removed from the screen. Default is nothing.
	 */
	public void onRemovedFromScreen() {
		// noop
	}

}
