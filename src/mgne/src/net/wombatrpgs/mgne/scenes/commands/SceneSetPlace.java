/**
 *  SceneWalk.java
 *  Created on Jul 25, 2014 1:13:41 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Usage: {@code walk(<event> or <eventName>, <x>, <y>)}
 */
public class SceneSetPlace extends VarArgFunction {
	
	/**
	 * @see org.luaj.vm2.lib.VarArgFunction#invoke(org.luaj.vm2.Varargs)
	 */
	@Override
	public Varargs invoke(final Varargs args) {
		SceneLib.addFunction(new SceneCommand() {
			
			LuaValue eventArg = args.arg(1);
			LuaValue luaX = args.arg(2);
			LuaValue luaY = args.arg(3);

			@Override protected void internalRun() {
				argToEvent(eventArg).setTileLocation(luaX.checkint(), luaY.checkint());
			}

			@Override protected boolean shouldFinish() {
				return true;
			}
			
		});
		return LuaValue.NIL;
	}
}
