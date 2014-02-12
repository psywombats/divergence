/**
 *  SagaGame.java
 *  Created on Feb 10, 2014 12:29:13 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.core;

import net.wombatrpgs.mgne.core.MgnGame;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.tactics.screen.TacticsScreen;

/**
 * One day, this class will tell MGNE how to run Sa- er, Tactics.
 */
public class TacticsGame extends MgnGame {

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeStarterScreen()
	 */
	@Override
	public Screen makeStarterScreen() {
		return new TacticsScreen();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#onCreate()
	 */
	@Override
	public void onCreate() {
		TGlobal.globalInit();
		super.onCreate();
	}

}
