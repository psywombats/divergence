/**
 *  Window.java
 *  Created on Apr 16, 2013 10:16:11 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen;

import net.wombatrpgs.mrogueschema.settings.WindowSettingsMDO;

/**
 * Thing that holds and calculates window values.
 */
public class WindowSettings {
	
	WindowSettingsMDO mdo;
	
	/**
	 * Constructs a new window settings from data.
	 * @param mdo
	 */
	public WindowSettings(WindowSettingsMDO mdo) {
		this.mdo = mdo;
	}
	
	/** @return The calculated width to draw at, in px */
	public int getWidth() {
		return mdo.resWidth;
	}

	/** @return The calculated height to draw at, in px */
	public int getHeight() {
		return mdo.resHeight;
	}
	
	/** @return The width of the window, in real px */
	public int getResolutionWidth() {
		return mdo.resWidth;
	}
	
	/** @return The height of the window, in real px */
	public int getResolutionHeight() {
		return mdo.resHeight;
	}
	
	/** @return The width of the window camera, in virtual px */
	public int getViewportWidth() {
		return mdo.width;
	}
	
	/** @return The height of the window camera, in virtual px */
	public int getViewportHeight() {
		return mdo.height;
	}
	
	/** @return The zoom factor appropriate for a camera */
	public float getZoom() {
		return (float) mdo.width / (float) mdo.resWidth;
	}
	
	/** @return The name of the game window */
	public String getTitle() {
		return mdo.windowName;
	}
}
