/**
 *  SceneSwitch.java
 *  Created on Aug 31, 2014 8:05:17 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Sets a switch on or off in the scene context.
 * Usage: {@code sceneSwitch(<switchname>, <state>)}
 */
public class SceneSwitch extends TwoArgFunction {

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg1, final LuaValue arg2) {
		SceneLib.addFunction(new SceneCommand() {
			
			String name = arg1.checkjstring();
			boolean value = arg2.checkboolean();
			
			@Override protected void internalRun() {
				MGlobal.memory.setSwitch(name, value);
			}
			
		});
		return LuaValue.NIL;
	}

}
