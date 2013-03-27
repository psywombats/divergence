/**
 *  InputCommand.java
 *  Created on Nov 22, 2012 3:30:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.io.data;

/**
 * A command represents an intention of the player, as demonstrated by their
 * input. It's more abstract than a simple keypress. Two maps should probably
 * exist, one on either side. At the moment this is literally mapped.
 */
public enum InputCommand {

	MOVE_START_UP,
	MOVE_START_DOWN,
	MOVE_START_LEFT,
	MOVE_START_RIGHT,
	
	MOVE_STOP_UP,
	MOVE_STOP_DOWN,
	MOVE_STOP_LEFT,
	MOVE_STOP_RIGHT,
	
	INTENT_CONFIRM,
	INTENT_CANCEL,
	INTENT_EXIT,
	
	ACTION_1,
	ACTION_2,
	ACTION_3,
	ACTION_4,
	
}
