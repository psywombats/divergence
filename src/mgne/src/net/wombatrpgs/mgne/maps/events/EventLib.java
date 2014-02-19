/**
 *  EventLib.java
 *  Created on Jan 28, 2014 1:02:43 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.events;

import net.wombatrpgs.mgne.core.MGlobal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
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
		env.set("teleport", new EventTeleport());
		
		env.set("eventlib", library);
		return library;
	}
	
	/**
	 * Teleport hero to a different map.
	 */
	private class EventTeleport extends ThreeArgFunction {
		@Override public LuaValue call(LuaValue map, LuaValue x, LuaValue y) {
			int tileX = x.checkint();
			int tileY = y.checkint();
			String mapName = map.checkjstring();
			MGlobal.levelManager.getTele().teleport(mapName, tileX, tileY);
			return LuaValue.NIL;
		}
	}

}
