/**
 *  InputCommand.java
 *  Created on Nov 22, 2012 3:30:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.io.data;

/**
 * A command represents an intention of the player, as demonstrated by their
 * input. It's more abstract than a simple keypress. Two maps should probably
 * exist, one on either side. At the moment this is literally mapped.
 */
public enum InputCommand {

	MOVE_NORTH,
	MOVE_NORTHEAST,
	MOVE_EAST,
	MOVE_SOUTHEAST,
	MOVE_SOUTH,
	MOVE_SOUTHWEST,
	MOVE_WEST,
	MOVE_NORTHWEST,
	MOVE_WAIT,
	
	ABIL_1,
	ABIL_2,
	ABIL_3,
	ABIL_4,
	ABIL_5,
	ABIL_6,
	
	INTENT_CONFIRM,
	INTENT_CANCEL,
	INTENT_QUIT,
	INTENT_FULLSCREEN,
	INTENT_INVENTORY,
	INTENT_LOOK,
	
	CURSOR_LEFT,
	CURSOR_RIGHT,
	CURSOR_UP,
	CURSOR_DOWN,
	
}
