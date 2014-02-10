/**
 *  EventLib.java
 *  Created on Jan 28, 2014 1:02:43 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps.events;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 *
 */
public class EventLib extends TwoArgFunction {
	
	/**
	 * Everybody needs a public constructor. Sorry, it's the rule!
	 */
	public EventLib() {
		
	}

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		// all event lib calls should be placed here
		// env.set("tint", new SceneTint());
		
		env.set("eventlib", library);
		return library;
	}

}
