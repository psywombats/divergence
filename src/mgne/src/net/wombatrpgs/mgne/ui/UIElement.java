/**
 *  UIElement.java
 *  Created on Oct 21, 2013 1:27:45 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.screen.ScreenObject;

/**
 * Superclass to wrap some common UI functionality. It really shouldn't do much
 * other than manage assets.
 */
public abstract class UIElement extends ScreenObject {
	
	/**
	 * Creates a new UI element on the first z layer above the map.
	 */
	public UIElement() {
		super(1);
	}
	
	/**
	 * Prepares this element for viewing in realtime. It's not that uncommon for
	 * UI elements so why not save some space? Although it's probably best to
	 * not use this for huge things.
	 */
	public void loadAssets() {
		MGlobal.loadAssets(assets, "assets ("+this+")");
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
		// TODO: ui: this should probably use the screen it's on right now
		return MGlobal.levelManager.getScreen().getUIBatch();
	}

}
