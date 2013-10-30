/**
 *  Platform.java
 *  Created on Oct 30, 2013 2:53:57 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

import com.badlogic.gdx.graphics.Pixmap;

/**
 * All the platform-specific things the engine needs.
 */
public abstract class Platform {
	
	/**
	 * Sets some icons. Only relevant on desktop mode. Default does nothing.
	 * @param	icons			The icons to set to, per libgdx format
	 */
	public void setIcon(Pixmap[] icons) {
		// noop
	}

}
