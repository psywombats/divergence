/**
 *  InputButton.java
 *  Created on Nov 19, 2012 2:32:43 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.io.data;

/**
 * An order that the player gives to the game via their input device. Sort of
 * meta-names for buttons rather than literal "scroll up" or "attack" or
 * whatever, though maybe that needs to be abstracted...
 */
public enum InputButton {
	
	UP,
	DOWN,
	LEFT,
	RIGHT,

	BUTTON_A,			// aka "confirm"
	BUTTON_B,			// aka "cancel"
	BUTTON_START,		// aka "menu"
	BUTTON_SELECT,		// aka who the hell uses this
	
	FULLSCREEN,
	DEBUG

}
