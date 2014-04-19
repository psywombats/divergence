/**
 *  WorldScreen.java
 *  Created on Mar 28, 2014 5:14:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.screen.instances.GameScreen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * Shows up when you wander the overworld.
 */
public class WorldScreen extends GameScreen {
	
	/**
	 * Creates a new world map screen.
	 */
	public WorldScreen() {
		
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.instances.GameScreen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
		case WORLD_PAUSE:
			MenuScreen menu = new MenuScreen();
			MGlobal.assets.loadAsset(menu, "main menu");
			MGlobal.screens.push(menu);
		default:
			return hero.onCommand(command);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (MGlobal.screens.peek() == this) {
			super.update(elapsed);
		}
	}

}
