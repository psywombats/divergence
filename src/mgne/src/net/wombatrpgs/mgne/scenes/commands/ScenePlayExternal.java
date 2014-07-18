/**
 *  ScenePlayExternal.java
 *  Created on 2014/07/17 17:13:47 for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/**
 * Play an external lua script file, relative to the scripts directory.
 * Usage: {@code play(<filename>)}
 */
public class ScenePlayExternal extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(LuaValue arg) {
		return SceneLib.spliceScene(arg.checkjstring());
	}

}
