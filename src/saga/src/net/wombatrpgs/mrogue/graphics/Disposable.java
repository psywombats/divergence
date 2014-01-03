/**
 *  Disposable.java
 *  Created on Feb 22, 2013 5:33:20 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

/**
 * Represents an asset that must be disposed.
 */
public interface Disposable {
	
	/**
	 * Called when the asset is destroyed, ala a C++ destructor. This thing is
	 * usually called when the game ends but could be called when a map goes out
	 * of scope or something.
	 */
	public void dispose();

}
