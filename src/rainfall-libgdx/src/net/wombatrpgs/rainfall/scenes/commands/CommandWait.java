/**
 *  CommandWait.java
 *  Created on Feb 5, 2013 9:21:06 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;

/**
 * Waits for a set period of time.
 */
public class CommandWait extends SceneCommand {
	
	protected float duration;
	protected float startTime;

	/**
	 * Creates a wait command from code.
	 * @param	parent			The executor that will kill, uh, run us
	 * @param 	line			The line of code that gave us life
	 */
	public CommandWait(SceneParser parent, String line) {
		super(parent, line);
		String arg = line.substring(line.indexOf(' ') + 1, line.indexOf(']'));
		duration = Float.valueOf(arg);
		startTime = -1;
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (startTime == -1) startTime = parent.getTimeSinceStart();
		return (startTime + duration <= parent.getTimeSinceStart());
	}

}
