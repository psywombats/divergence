/**
 *  CommandWait.java
 *  Created on Feb 5, 2013 9:21:06 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * Waits for a set period of time.
 */
public class CommandWait extends SceneCommand {
	
	protected static final String ARG_SOFT = "soft";
	
	protected float duration;
	protected float startTime;
	protected boolean soft, running;;

	/**
	 * Creates a wait command from code.
	 * @param	parent			The executor that will kill, uh, run us
	 * @param 	line			The line of code that gave us life
	 */
	public CommandWait(SceneParser parent, String line) {
		super(parent, line);
		startTime = -1;
		soft = false;
		running = false;
		String arg = line.substring(line.indexOf(' ') + 1, line.indexOf(']'));
		if (arg.indexOf(' ') != -1) {
			duration = Float.valueOf(arg.substring(0, arg.indexOf(' ')));
			arg = arg.substring(arg.indexOf(' ' ) + 1);
			if (arg.equals(ARG_SOFT)) {
				soft = true;
				MGlobal.reporter.warn("Soft pause is not fully implemented");
			} else {
				MGlobal.reporter.warn("Unknown wait argument: " + arg);
			}
		} else {
			duration = Float.valueOf(arg);
		}

	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		if (!running) {
			startTime = parent.getTimeSinceStart();
			//if (soft) parent.getParent().setPause(false);
			running = true;
		}
		if (startTime + duration <= parent.getTimeSinceStart()) {
			finished = true;
			//if (soft) parent.getParent().setPause(true);
		}
		return false;
	}

}
