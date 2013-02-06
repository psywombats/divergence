/**
 *  CommandFactory.java
 *  Created on Feb 3, 2013 9:59:52 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.scenes.commands.CommandCameraPan;
import net.wombatrpgs.rainfall.scenes.commands.CommandCameraSpeed;
import net.wombatrpgs.rainfall.scenes.commands.CommandCameraTrack;
import net.wombatrpgs.rainfall.scenes.commands.CommandMove;
import net.wombatrpgs.rainfall.scenes.commands.CommandSpeak;
import net.wombatrpgs.rainfall.scenes.commands.CommandWaitAll;

/**
 * A static thing that given a line from a scene, produces a command appropriate
 * for that line.
 */
public class CommandFactory {
	
	protected static final String COMMAND_MOVE = "move";
	protected static final String COMMAND_WAIT_ALL = "wait-all";
	protected static final String COMMAND_CAM_TRACK = "camera-track";
	protected static final String COMMAND_CAM_SPEED = "camera-speed";
	protected static final String COMMAND_CAM_PAN = "camera-pan";
	
	/**
	 * Actually performs the generation. This should be a great big if/else.
	 * @param 	parent			The parent parser that will run the command
	 * @param 	line			The line read from the string file
	 * @return					The generated command
	 */
	public static SceneCommand make(SceneParser parent, String line) {
		if (line.equals("")) return null;
		if (line.startsWith("[")) {
			String commandName;
			if (line.indexOf(' ') > 0) {
				commandName = line.substring(1, line.indexOf(' '));
			} else {
				commandName = line.substring(1, line.indexOf(']'));
			}
			if (commandName.equals(COMMAND_MOVE)) {
				return new CommandMove(parent, line);
			} else if (commandName.equals(COMMAND_WAIT_ALL)) {
				return new CommandWaitAll(parent, line);
			} else if (commandName.equals(COMMAND_CAM_TRACK)) {
				return new CommandCameraTrack(parent, line);
			} else if (commandName.equals(COMMAND_CAM_SPEED)) {
				return new CommandCameraSpeed(parent, line);
			} else if (commandName.equals(COMMAND_CAM_PAN)) {
				return new CommandCameraPan(parent, line);
			} else {
				RGlobal.reporter.warn("Did not recognize a command: " + commandName);
				return null;
			}
		} else if (line.contains(":")) {
			return new CommandSpeak(parent, line);
		} else {
			RGlobal.reporter.warn("Found something weird in a scene file: " + line);
			return null;
		}
	}

}
