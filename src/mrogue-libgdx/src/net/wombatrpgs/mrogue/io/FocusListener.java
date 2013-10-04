/**
 *  FocusListener.java
 *  Created on Nov 26, 2012 2:24:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

/**
 * Listens if focus is gained or lost. Really only useful on the desktop.
 */
public interface FocusListener {
	
	/**
	 * Called when window focus is lost.
	 */
	public void onFocusLost();
	
	/**
	 * Called when window focus is gained.
	 */
	public void onFocusGained();

}
