/**
 *  CommandToggleHud.java
 *  Created on Feb 6, 2013 2:23:09 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfall.ui.Hud;

/**
 * Shows or hides the HUD.
 */
public class CommandToggleHud extends SceneCommand {
	
	protected static final String TOGGLE_ON = "on";
	protected static final String TOGGLE_OFF = "off";
	
	protected boolean turnOn, turnOff;

	/**
	 * Creates a command from code.
	 * @param 	parent			The parser that spawned us
	 * @param	line			The code that spawned us
	 */
	public CommandToggleHud(SceneParser parent, String line) {
		super(parent, line);
		String arg = line.substring(line.indexOf(' ') + 1, line.indexOf(']'));
		if (arg.equals(TOGGLE_ON)) turnOn = true;
		else if (arg.equals(TOGGLE_OFF)) turnOff = true;
		else RGlobal.reporter.warn("Invalid toggle hud arg: " + arg);
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			finished = true;
			Hud hud = RGlobal.ui.getHud();
			if (turnOn && !hud.isEnabled()) {
				RGlobal.screens.peek().addPicture(hud);
				hud.setEnabled(true);
			} else if (turnOff && hud.isEnabled()) {
				RGlobal.screens.peek().removePicture(hud);
				hud.setEnabled(false);
			}
		}
		return true;
	}

}
