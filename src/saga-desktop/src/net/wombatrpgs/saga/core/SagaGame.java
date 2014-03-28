/**
 *  SagaGame.java
 *  Created on Feb 10, 2014 12:29:13 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.MgnGame;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.saga.screen.WorldScreen;

/**
 * One day, this class will tell MGNE how to run Saga.
 */
public class SagaGame extends MgnGame {

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeStarterScreen()
	 */
	@Override
	public Screen makeStarterScreen() {
		WorldScreen screen = new WorldScreen();
		MGlobal.assets.loadAsset(screen, "world screen");
		return screen;
	}

}
