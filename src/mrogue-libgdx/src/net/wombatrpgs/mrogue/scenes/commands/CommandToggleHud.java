/**
 *  CommandToggleHud.java
 *  Created on Feb 6, 2013 2:23:09 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.ui.Hud;

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
		else MGlobal.reporter.warn("Invalid toggle hud arg: " + arg);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			finished = true;
			Hud hud = MGlobal.ui.getHud();
			if (turnOn && !hud.isEnabled()) {
				MGlobal.screens.peek().addObject(hud);
				hud.setEnabled(true);
			} else if (turnOff && hud.isEnabled()) {
				MGlobal.screens.peek().removeObject(hud);
				hud.setEnabled(false);
			}
		}
		return true;
	}

}
