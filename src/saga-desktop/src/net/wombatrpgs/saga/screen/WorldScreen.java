/**
 *  WorldScreen.java
 *  Created on Mar 28, 2014 5:14:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import net.wombatrpgs.mgne.screen.instances.GameScreen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * Shows up when you wander the overworld.
 */
public class WorldScreen extends GameScreen {

	/**
	 * @see net.wombatrpgs.mgne.screen.instances.GameScreen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
		default:
			return hero.onCommand(command);
		}
	}

}
