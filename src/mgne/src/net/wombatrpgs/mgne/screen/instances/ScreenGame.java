/**
 *  GameScreen.java
 *  Created on Feb 22, 2013 4:13:09 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen.instances;

import net.wombatrpgs.mgne.io.command.CMapGame;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * This is the default screen that appears when the game is first loaded. Kind
 * of a placeholder class for the subgame and can be overridden as needed.
 */
public class ScreenGame extends Screen {
	
	/**
	 * Constructs the introductory screen.
	 */
	public ScreenGame() {
		pushCommandContext(new CMapGame());
	}
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
			// engine-wide commands go here
		default:
			return false;
		}
	}

}
