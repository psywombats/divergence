/**
 *  CommandFactory.java
 *  Created on Feb 3, 2013 9:59:52 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.scenes.commands.CommandBlock;
import net.wombatrpgs.mrogue.scenes.commands.CommandCameraPan;
import net.wombatrpgs.mrogue.scenes.commands.CommandCameraSpeed;
import net.wombatrpgs.mrogue.scenes.commands.CommandCameraTrack;
import net.wombatrpgs.mrogue.scenes.commands.CommandGoToTitle;
import net.wombatrpgs.mrogue.scenes.commands.CommandMove;
import net.wombatrpgs.mrogue.scenes.commands.CommandPlayBGM;
import net.wombatrpgs.mrogue.scenes.commands.CommandPlaySound;
import net.wombatrpgs.mrogue.scenes.commands.CommandSetGraphic;
import net.wombatrpgs.mrogue.scenes.commands.CommandSetLocation;
import net.wombatrpgs.mrogue.scenes.commands.CommandSetSwitch;
import net.wombatrpgs.mrogue.scenes.commands.CommandShowHide;
import net.wombatrpgs.mrogue.scenes.commands.CommandSpeakAll;
import net.wombatrpgs.mrogue.scenes.commands.CommandTeleport;
import net.wombatrpgs.mrogue.scenes.commands.CommandTint;
import net.wombatrpgs.mrogue.scenes.commands.CommandToggleHud;
import net.wombatrpgs.mrogue.scenes.commands.CommandWait;
import net.wombatrpgs.mrogue.scenes.commands.CommandWaitAll;

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
	protected static final String COMMAND_WAIT = "wait";
	protected static final String COMMAND_TINT = "tint";
	protected static final String COMMAND_TOGGLE_HUD = "toggle-hud";
	protected static final String COMMAND_TELE = "teleport";
	protected static final String COMMAND_SHOW = "show";
	protected static final String COMMAND_HIDE = "hide";
	protected static final String COMMAND_SET_GRAPHIC = "change-graphic";
	protected static final String COMMAND_SET_LOCATION = "set-location";
	protected static final String COMMAND_SET_SWITCH = "set-switch";
	protected static final String COMMAND_GO_TO_TITLE = "goto-title";
	protected static final String COMMAND_PLAY_SOUND = "play-sound";
	protected static final String COMMAND_PLAY_BGM = "play-bgm";
	protected static final String COMMAND_BLOCK = "pause";
	
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
			} else if (commandName.equals(COMMAND_PLAY_BGM)) {
				return new CommandPlayBGM(parent, line);
			} else if (commandName.equals(COMMAND_BLOCK)) {
				return new CommandBlock(parent, line);
			} else if (commandName.equals(COMMAND_PLAY_SOUND)) {
				return new CommandPlaySound(parent, line);
			} else if (commandName.equals(COMMAND_GO_TO_TITLE)) {
				return new CommandGoToTitle(parent, line);
			} else if (commandName.equals(COMMAND_SET_SWITCH)) {
				return new CommandSetSwitch(parent, line);
			} else if (commandName.equals(COMMAND_SET_LOCATION)) {
				return new CommandSetLocation(parent, line);
			} else if (commandName.equals(COMMAND_SET_GRAPHIC)) {
				return new CommandSetGraphic(parent, line);
			} else if (commandName.equals(COMMAND_WAIT_ALL)) {
				return new CommandWaitAll(parent, line);
			} else if (commandName.equals(COMMAND_SHOW)) {
				return new CommandShowHide(parent, line, false);
			} else if (commandName.equals(COMMAND_HIDE)) {
				return new CommandShowHide(parent, line, true);
			} else if (commandName.equals(COMMAND_CAM_TRACK)) {
				return new CommandCameraTrack(parent, line);
			} else if (commandName.equals(COMMAND_CAM_SPEED)) {
				return new CommandCameraSpeed(parent, line);
			} else if (commandName.equals(COMMAND_CAM_PAN)) {
				return new CommandCameraPan(parent, line);
			} else if (commandName.equals(COMMAND_WAIT)) {
				return new CommandWait(parent, line);
			} else if (commandName.equals(COMMAND_TINT)) {
				return new CommandTint(parent, line);
			} else if (commandName.equals(COMMAND_TOGGLE_HUD)) {
				return new CommandToggleHud(parent, line);
			} else if (commandName.equals(COMMAND_TELE)) {
				return new CommandTeleport(parent, line);
			} else {
				MGlobal.reporter.warn("Did not recognize a command: " + commandName);
				return null;
			}
		} else if (line.contains(":")) {
			return new CommandSpeakAll(parent, line);
		} else {
			MGlobal.reporter.warn("Found something weird in a scene file: " + line);
			return null;
		}
	}

}
