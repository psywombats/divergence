/**
 *  ButtonListener.java
 *  Created on Nov 23, 2012 3:25:02 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import net.wombatrpgs.mrogueschema.io.data.InputButton;

/**
 * Listens to meta-buttons. Simple use of observer pattern. Listeners can expect
 * that meta-buttons that may be analog or discrete (as opposed to up-down
 * physical buttons) on the input device will send a down immediately followed
 * by an up.
 */
public interface ButtonListener {
	
	/**
	 * Called whenever the specified meta-button is pressed down.
	 * @param 	button		The meta-button that was pressed
	 */
	public void onButtonPressed(InputButton button);
	
	/**
	 * Called whenever the specified meta-button is released after being down
	 * @param 	button		The meta-button that was released
	 */
	public void onButtonReleased(InputButton button);

}
