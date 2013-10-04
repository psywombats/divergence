/**
 *  CommandWaitAll.java
 *  Created on Feb 5, 2013 12:40:04 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * Wait until all characters that the parser is controlling have resumed their
 * movements.
 */
public class CommandWaitAll extends SceneCommand {

	/**
	 * Creates a new command to execute later.
	 * @param 	parent			The parser that will execute us
	 * @param 	line			The line of code that spawned us
	 */
	public CommandWaitAll(SceneParser parent, String line) {
		super(parent, line);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		for (MapEvent event : parent.getControlledEvents()) {
			if (event.isTracking() || event.isMoving()) return false;
		}
		finished = true;
		for (MapEvent event : parent.getControlledEvents()) {
			event.halt();
		}
		return true;
	}

}
