/**
 *  BaconLib.java
 *  Created on Feb 6, 2015 5:39:14 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.core;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * goddamn bacon
 */
public class BaconLib extends TwoArgFunction {

	@Override public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		env.set("hasItem", new OneArgFunction() {
			@Override public LuaValue call(LuaValue arg) {
				String itemName = arg.checkjstring();
				if (BGlobal.items.contains(itemName)) {
					return LuaValue.TRUE;
				} else {
					return LuaValue.FALSE;
				}
			}
		});

		env.set("baconlib", library);
		return library;
	}
	
}
