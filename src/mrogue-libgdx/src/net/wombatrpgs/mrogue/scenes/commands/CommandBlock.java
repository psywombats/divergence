/**
 *  CommandBlock.java
 *  Created on Mar 29, 2013 1:00:43 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * Waits for the user to hit the spacebar. Or dialog button. Whatever.
 */
public class CommandBlock extends SceneCommand {
	
	protected boolean running;

	/**
	 * Inherited constructor.
	 * @param 	parent			The parent parser
	 * @param 	line			The line of code
	 */
	public CommandBlock(SceneParser parent, String line) {
		super(parent, line);
		running  = false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		if (running) {
			if (!blocking) {
				finished = true;
				return true;
			} else {
				return false;
			}
		} else {
			running = true;
			block(new UnblockedListener() {
				@Override public void onUnblock() { }
			});
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		running = false;
	}

}
