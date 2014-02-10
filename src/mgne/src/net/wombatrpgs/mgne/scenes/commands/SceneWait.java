/**
 *  SceneWait.java
 *  Created on Jan 24, 2014 2:49:05 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

/**
 * Waits for a certain amount of time to elapse. Given in seconds.
 * Usage: {@code wait(<time>)}
 */
public class SceneWait extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			@Override protected void internalRun() {
				waitFor(arg.tofloat());
			}
		});
		return LuaValue.NIL;
	}

}
