/**
 *  Lua.java
 *  Created on Jan 24, 2014 1:22:57 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.saga.scenes.SceneLib;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.files.FileHandle;

/**
 * Anything and everything lua. I don't know much about lua so this is going to
 * look a little empty for a bit.
 */
public class Lua {
	
	protected Globals globals;
	protected List<Class<? extends TwoArgFunction>> libClasses;
	
	/**
	 * Creates and intializes the lua manager. This also sets up all required
	 * functions for use in lualand.
	 */
	public Lua() {
		globals = JsePlatform.standardGlobals();
		libClasses = new ArrayList<Class<? extends TwoArgFunction>>();
		libClasses.add(SceneLib.class);
	}
	
	/**
	 * Loads the contents of a file as a lua value.
	 * @param	file				The file to load, a lua source
	 * @return						The lua script in that file
	 */
	public LuaValue load(FileHandle file) {
		return globals.load(prependRequires(file.readString()), file.name());
	}
	
	/**
	 * Evaluates a chunk of text, interpreting it as a Lua script.
	 * @param	chunk			The text to interpret
	 * @return					A corresponding script, ready to run
	 */
	public LuaValue eval(String chunk) {
		return globals.load(prependRequires(chunk), "Lua.eval");
	}
	
	/**
	 * Prepends a few lines dealing with library includes to a chunk of Lua
	 * code. This is probably called on all stuff being passed in from external
	 * files to MGN.
	 * @param	chunk			The chunk to modify
	 * @return					That same chunk, but with lib requires prepended
	 */
	protected String prependRequires(String chunk) {
		chunk = chunk + "\n";
		for (Class<? extends TwoArgFunction> clazz : libClasses) {
			String require = "require('" + clazz.getName() + "')\n";
			chunk = require + chunk;
		}
		return chunk;
	}

}
