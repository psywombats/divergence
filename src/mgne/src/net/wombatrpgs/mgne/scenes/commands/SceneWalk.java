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
 * Moves a map event across the map. The final parameter will halt execution
 * until the character reaches their destination. Like all scenelib commands,
 * these do not execute immediately but rather wait for their sequence in a
 * scripted scene. The event provided can be an event name which will be
 * looked up at time of execution.
 * Usage: {@code walk(<event> or <eventName>, <steps>, <direction>, [wait])}
 */
public class SceneWalk extends VarArgFunction {
	/**
	 * @see org.luaj.vm2.lib.VarArgFunction#invoke(org.luaj.vm2.Varargs)
	 */
	@Override
	public Varargs invoke(final Varargs args) {
		SceneLib.addFunction(new SceneCommand() {
			
			LuaValue eventArg = args.arg(1);
			LuaValue steps = args.arg(2);
			LuaValue dir = args.arg(3);
			boolean wait = args.narg() >= 4 ? args.checkboolean(4) : true;

			@Override protected void internalRun() {
				eventLua(eventArg).get("eventWalk").call(steps, dir);
			}

			@Override protected boolean shouldFinish() {
				if (!super.shouldFinish()) return false;
				if (wait) {
					return !eventLua(eventArg).get("isTracking").call().checkboolean();
				} else {
					return true;
				}
			}
			
		});
		return LuaValue.NIL;
	}
}
