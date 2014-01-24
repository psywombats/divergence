/**
 *  LFunc.java
 *  Created on Jan 24, 2014 1:17:26 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.saga.scenes.commandslua.SceneTint;
import net.wombatrpgs.saga.scenes.commandslua.SceneWait;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Any function that can be called by the Lua scripts as part of a scene.
 */
public class SceneLib extends TwoArgFunction {
	
	protected static List<SceneCommandLua> commands;
	
	/**
	 * Everybody needs a public constructor. Sorry, it's the rule!
	 */
	public SceneLib() {
		
	}

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		env.set("tint", new SceneTint());
		env.set("wait", new SceneWait());
		
		env.set("scenelib", library);
		return library;
	}
	
	/**
	 * Constructs a scene from a script. The list of commands can then be run
	 * as necessary.
	 * @param	script			The script to turn into a command list
	 * @return					A list of call commands generated by that script
	 */
	public static List<SceneCommandLua> parseScene(LuaValue script) {
		commands = new ArrayList<SceneCommandLua>();
		script.call();
		return commands;
	}
	
	/**
	 * Adds a command to the list of commands that have been parsed by scenelib
	 * but not yet executed. Should be called at the end of every scenelib
	 * function.
	 * @param	command			The command to be run later
	 */
	public static void addFunction(SceneCommandLua command) { commands.add(command); }

}
