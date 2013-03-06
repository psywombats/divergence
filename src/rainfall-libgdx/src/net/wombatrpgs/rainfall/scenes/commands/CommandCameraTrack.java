/**
 *  CommandCamera.java
 *  Created on Feb 5, 2013 7:53:53 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;

/**
 * Commands to manipulate the camera.
 */
public class CommandCameraTrack extends SceneCommand {
	
	protected static final String NULL_TARGET = "fix";
	
	protected MapEvent target;
	protected String arg;

	/**
	 * Creates a new camera command from code.
	 * @param 	parent			The parser that will execute this command
	 * @param 	line
	 */
	public CommandCameraTrack(SceneParser parent, String line) {
		super(parent, line);
		arg = line.substring(line.indexOf(' ')+1, line.indexOf(']'));
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			if (arg.equals(NULL_TARGET)) {
				target = null;
			} else {
				target = parent.getLevel().getEventByName(arg);
				if (target == null) {
					RGlobal.reporter.warn("Couldn't find event to track named: " + arg);
				}
			}
			RGlobal.screens.getCamera().track(target);
			finished = true;
		}
		return true;
	}

}
