/**
 *  FinishListener.java
 *  Created on Feb 12, 2013 7:44:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes;

import net.wombatrpgs.mrogue.maps.Level;

/**
 * Called when a scene is done parsing.
 */
public interface FinishListener {
	
	/**
	 * Called when the scene ends.
	 * @param	map				The level the scene was on
	 */
	public void onFinish(Level map);

}
