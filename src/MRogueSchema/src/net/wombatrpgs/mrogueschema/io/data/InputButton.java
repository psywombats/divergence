/**
 *  InputButton.java
 *  Created on Nov 19, 2012 2:32:43 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.io.data;

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
	
	DIR_N,
	DIR_NE,
	DIR_E,
	DIR_SE,
	DIR_S,
	DIR_SW,
	DIR_W,
	DIR_NW,
	WAIT,
	
	BUTTON_1,			// aka "confirm"
	BUTTON_2,			// aka "cancel"
	BUTTON_3,
	BUTTON_4,
	BUTTON_5,
	BUTTON_6,
	
	ABIL_1,
	ABIL_2,
	ABIL_3,
	ABIL_4,
	ABIL_5,
	ABIL_6,
	
	
	MENU,
	FULLSCREEN,
	TAB,
	LOOK,

}
