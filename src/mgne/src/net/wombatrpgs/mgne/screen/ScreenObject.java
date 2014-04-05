/**
 *  Canvasable.java
 *  Created on Jan 30, 2013 1:00:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Renderable;

/**
 * Something that's both renderable and updateable. Can also be sorted.
 */
public abstract class ScreenObject implements	Renderable, 
												Updateable  {
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a blank screen object.
	 */
	public ScreenObject() {
		this.assets = new ArrayList<Queueable>();
	}

	/**
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
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
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
	 * Default is nothing.
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// noop
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
	 * If this is true, ignores the screen's transition tint. This means that
	 * it will have to be rendered in a separate phase.
	 * @return					True if tint is ignored, false if it is applied
	 */
	public boolean ignoresTint() {
		return false;
	}
	
	/**
	 * Called when this object is added to the screen. Default is nothing.
	 * @param	screen			The screen we just got added to
	 */
	public void onAddedToScreen(Screen screen) {
		// noop
	}
	
	/**
	 * Called when this object is removed from the screen. Default is nothing.
	 * @param	screen			The screen we just got stolen from
	 */
	public void onRemovedFromScreen(Screen screen) {
		// noop
	}

}
