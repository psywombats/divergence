/**
 *  LFunc.java
 *  Created on Jan 24, 2014 1:17:26 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.lua.LoadableScript;
import net.wombatrpgs.mgne.scenes.commands.SceneFace;
import net.wombatrpgs.mgne.scenes.commands.SceneHideAvatar;
import net.wombatrpgs.mgne.scenes.commands.ScenePathToEvent;
import net.wombatrpgs.mgne.scenes.commands.ScenePathToTile;
import net.wombatrpgs.mgne.scenes.commands.ScenePlayExternal;
import net.wombatrpgs.mgne.scenes.commands.SceneSound;
import net.wombatrpgs.mgne.scenes.commands.SceneSpeak;
import net.wombatrpgs.mgne.scenes.commands.SceneSwitch;
import net.wombatrpgs.mgne.scenes.commands.SceneTeleport;
import net.wombatrpgs.mgne.scenes.commands.SceneTint;
import net.wombatrpgs.mgne.scenes.commands.SceneWait;
import net.wombatrpgs.mgne.scenes.commands.SceneWalk;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Any function that can be called by the Lua scripts as part of a scene.
 */
public class SceneLib extends TwoArgFunction {
	
	protected static List<SceneCommand> commands;

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		
		env.set("teleport", new SceneTeleport());
		env.set("tint", new SceneTint());
		env.set("wait", new SceneWait());
		env.set("speak", new SceneSpeak());
		env.set("play", new ScenePlayExternal());
		env.set("walk", new SceneWalk());
		env.set("face", new SceneFace());
		env.set("hideHero", new SceneHideAvatar());
		env.set("sceneSwitch", new SceneSwitch());
		env.set("path", new ScenePathToTile());
		env.set("pathEvent", new ScenePathToEvent());
		env.set("playSound", new SceneSound());
		
		env.set("scenelib", library);
		return library;
	}
	
	/**
	 * Constructs a scene from a script. The list of commands can then be run
	 * as necessary.
	 * @param	script			The script to turn into a command list
	 * @param	caller			The lua value to set as the this
	 * @return					A list of call commands generated by that script
	 */
	public static List<SceneCommand> parseScene(LuaValue script, LuaValue caller) {
		commands = new ArrayList<SceneCommand>();
		MGlobal.lua.run(script, caller);
		return commands;
	}
	
	/**
	 * Splices in a scene from an external file. This should only be called from
	 * scenes in the context of parsing a commands list.
	 * @param	filename		The name of the file to splice in, without dir
	 * @return					The result of the evaluation, usually nil
	 */
	public static LuaValue spliceScene(String filename) {
		LoadableScript script = new LoadableScript(filename);
		MGlobal.assets.loadAsset(script, "spliced script " + filename);
		return script.getScript().call();
	}
	
	/**
	 * Adds a command to the list of commands that have been parsed by scenelib
	 * but not yet executed. Should be called at the end of every scenelib
	 * function.
	 * @param	command			The command to be run later
	 */
	public static void addFunction(SceneCommand command) {
		commands.add(command);
	}

}
