/**
 *  InputCommand.java
 *  Created on Nov 22, 2012 3:30:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.io.data;

/**
 * A command represents an intention of the player, as demonstrated by their
 * input. It's more abstract than a simple keypress. Two maps should probably
 * exist, one on either side. At the moment this is literally mapped.
 */
public enum InputCommand {

	MOVE_UP,
	MOVE_LEFT,
	MOVE_DOWN,
	MOVE_RIGHT,
	MOVE_STOP,
	
	WORLD_INTERACT,
	WORLD_PAUSE,
	
	UI_CONFIRM,
	UI_CANCEL,
	
	GLOBAL_FULLSCREEN,
	
}
