/**
 *  SagaEventLib.java
 *  Created on Aug 27, 2014 12:26:34 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.items.CombatItem;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Crappy library with utility calls for event scripting.
 */
public class SagaEventLib extends TwoArgFunction {

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		env.set("getHero", new OneArgFunction() {
			@Override public LuaValue call(LuaValue slotArg) {
				int slot = slotArg.checkint();
				if (slot >= SGlobal.heroes.size()) {
					return LuaValue.NIL;
				} else {
					return SGlobal.heroes.getFront(slot).toLua();
				}
			}
		});
		
		env.set("hasItem", new OneArgFunction() {
			@Override public LuaValue call(LuaValue itemArg) {
				String key = itemArg.checkjstring();
				return SGlobal.heroes.isCarryingItemType(key) ? LuaValue.TRUE : LuaValue.FALSE;
			}
		});
		
		env.set("addItem", new OneArgFunction() {
			@Override public LuaValue call(LuaValue itemArg) {
				String key = itemArg.checkjstring();
				CombatItem item = new CombatItem(key);
				if (!SGlobal.heroes.getInventory().isFull()) {
					SGlobal.heroes.addItem(item);
				}
				return LuaValue.NIL;
			}
		});
		
		env.set("sagaeventlib", library);
		return library;
	}

}
