/**
 *  UIElement.java
 *  Created on Oct 21, 2013 1:27:45 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.screen.ScreenShowable;

/**
 * Superclass to wrap some common UI functionality. It really shouldn't do much
 * other than manage assets.
 */
public abstract class UIElement implements	ScreenShowable,
											Queueable {
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new UI element.
	 */
	public UIElement() {
		assets = new ArrayList<Queueable>();
	}

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
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenShowable#ignoresTint()
	 */
	@Override
	public boolean ignoresTint() {
		return false;
	}
	
	/**
	 * Default does nothing.
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// noop
	}
	
	/**
	 * Initializes a graphic from file name and then adds it to assets.
	 * @param 	fileName		The name of the file to load
	 * @return					The created graphic
	 */
	protected Graphic startGraphic(String fileName) {
		Graphic graphic = new Graphic(fileName);
		assets.add(graphic);
		return graphic;
	}

	/**
	 * Gets the batch we should be using to render.
	 * @return					The UI batch to render with
	 */
	protected SpriteBatch getBatch() {
		return MGlobal.screens.peek().getUIBatch();
	}

}
