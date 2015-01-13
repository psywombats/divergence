/**
 *  SceneReset.java
 *  Created on Jan 12, 2015 12:28:00 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.screen.SagaScreen;
import net.wombatrpgs.saga.screen.ScreenTitle;
import net.wombatrpgs.saga.screen.SagaScreen.TransitionType;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Resets the game back to the title screne.
 */
public class SceneReset extends ZeroArgFunction {

	/**
	 * @see org.luaj.vm2.lib.ZeroArgFunction#call()
	 */
	@Override
	public LuaValue call() {
		SceneLib.addFunction(new SceneCommand() {
			
			@Override protected void internalRun() {
				SagaScreen title = new ScreenTitle();
				MGlobal.assets.loadAsset(title,  "new title");
				MGlobal.levelManager.reset();
				SGlobal.heroes = null;
				title.transitonOn(TransitionType.WHITE, null);
			}
			
		});
		return LuaValue.NIL;
	}

}
