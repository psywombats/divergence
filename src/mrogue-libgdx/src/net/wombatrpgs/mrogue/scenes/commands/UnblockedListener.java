/**
 *  UnblockedListener.java
 *  Created on Feb 4, 2013 4:16:18 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

/**
 * Called when a command stops blocking.
 */
public interface UnblockedListener {
	
	/**
	 * Called when a command stops blocking.
	 */
	public void onUnblock();

}
