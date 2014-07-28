/**
 *  MgnLib.java
 *  Created on Jul 25, 2014 7:07:59 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.lua;

import net.wombatrpgs.mgne.core.MGlobal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Library of core MGN functions for Lua. These are designed to evaluate
 * immediately rather than SceneLib's delayed response thing.
 */
public class MgnLib extends TwoArgFunction {

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call(org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		env.set("getSwitch", new OneArgFunction() {
			@Override public LuaValue call(LuaValue arg) {
				boolean value = MGlobal.memory.getSwitch(arg.checkjstring());
				return CoerceJavaToLua.coerce(value);
			}
		});
		env.set("setSwitch", new TwoArgFunction() {
			@Override public LuaValue call(LuaValue arg1, LuaValue arg2) {
				boolean value = arg2.checkboolean();
				String switchName = arg1.checkjstring();
				boolean oldValue = MGlobal.memory.getSwitch(switchName);
				MGlobal.memory.setSwitch(switchName, value);
				return CoerceJavaToLua.coerce(oldValue);
			}
		});
		
		env.set("mgnlib", library);
		return library;
	}

}
