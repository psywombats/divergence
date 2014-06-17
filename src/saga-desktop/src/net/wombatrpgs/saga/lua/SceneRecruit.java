/**
 *  SceneRecruit.java
 *  Created on Jun 16, 2014 1:48:51 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.screen.ScreenName;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Allows the player to select a new character to add to their party.
 */
public class SceneRecruit extends ZeroArgFunction {

	/**
	 * @see org.luaj.vm2.lib.ZeroArgFunction#call()
	 */
	@Override
	public LuaValue call() {
		SceneLib.addFunction(new SceneCommand() {

			ScreenName nameScreen;

			@Override protected void internalRun() {
				// battle stuff moved here, problems before with null heroes
				nameScreen = new ScreenName(SGlobal.heroes.getFront());
				MGlobal.assets.loadAsset(nameScreen, "name screen");
				MGlobal.screens.push(nameScreen);
			}

			@Override protected void finish() {
				super.finish();
				nameScreen.dispose();
			}

			@Override protected boolean shouldFinish() {
				return super.shouldFinish() && nameScreen.isDone();
			}
			
		});
		
		return LuaValue.NIL;
	}

}
