/**
 *  SceneInn.java
 *  Created on May 20, 2014 1:08:03 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.screen.InnScreen;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Calls the stay at the inn screen.
 */
public class SceneInn extends ZeroArgFunction {

	/**
	 * @see org.luaj.vm2.lib.ZeroArgFunction#call()
	 */
	@Override
	public LuaValue call() {
		SceneLib.addFunction(new SceneCommand() {

			InnScreen inn;

			@Override protected void internalRun() {
				// battle stuff moved here, problems before with null heroes
				inn = new InnScreen();
				MGlobal.assets.loadAsset(inn, "scene inn");
				MGlobal.screens.push(inn);
			}

			@Override protected void finish() {
				// the inn will remove itself
				super.finish();
				inn.dispose();
			}

			@Override protected boolean shouldFinish() {
				return super.shouldFinish() && inn.isDone();
			}
			
		});
		return LuaValue.NIL;
	}

}
