/**
 *  CommandCameraSpeed.java
 *  Created on Feb 5, 2013 8:16:21 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * Sets the camera pan speed.
 */
public class CommandCameraSpeed extends SceneCommand {
	
	protected int speed;

	/**
	 * Creates a new command from code;
	 * @param 	parent			The parent parser
	 * @param 	line			The line of code generated from
	 */
	public CommandCameraSpeed(SceneParser parent, String line) {
		super(parent, line);
		String arg = line.substring(line.indexOf(' ') + 1, line.indexOf(']'));
		speed = Integer.valueOf(arg);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			finished = true;
			MGlobal.screens.getCamera().setPanSpeed(speed);
		}
		return true;
	}

}
