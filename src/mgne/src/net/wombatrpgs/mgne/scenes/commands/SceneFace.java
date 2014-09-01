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
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Makes some event face a direction. Again, this is the version with scenelib
 * direct integration, it doesn't play immediately. This could be replaced with
 * a function to just delay a script execution. The event can be replaced with
 * an event name that will be looked up at time of execution.
 * Usage: {@code walk(<event> or <eventName>, <direction>)}
 */
public class SceneFace extends TwoArgFunction {

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue eventArg, final LuaValue dirArg) {
		SceneLib.addFunction(new SceneCommand() {
			
			LuaValue event = eventArg;
			LuaValue dir = dirArg;

			@Override protected void internalRun() {
				eventLua(event).get("eventFace").call(dir);
			}
			
		});
		return LuaValue.NIL;
	}
	
}
