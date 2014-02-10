/**
 *  ButtonListener.java
 *  Created on Nov 23, 2012 3:25:02 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.io;

/**
 * Listens to meta-buttons. Simple use of observer pattern. Listeners can expect
 * that meta-buttons that may be analog or discrete (as opposed to up-down
 * physical buttons) on the input device will send a down immediately followed
 * by an up.
 */
public interface ButtonListener {
	
	/**
	 * Called whenever the a meta-button event (press, release, hold) occurs.
	 * @param 	event			The input event that occurred
	 */
	public void onEvent(InputEvent event);

}
