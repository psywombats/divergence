/**
 *  Lua.java
 *  Created on Jan 24, 2014 1:22:57 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.lua;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
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
		
		// ADD ALL LIBRARIES HERE
		libClasses.add(SceneLib.class);
		libClasses.addAll(MGlobal.game.getLuaLibs());
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
	 * Evaluates a chunk of text, interpreting it as a Lua script. Does not
	 * actually run the chunk.
	 * @param	chunk			The text to interpret
	 * @return					A corresponding script, ready to run
	 */
	public LuaValue interpret(String chunk) {
		return globals.load(prependRequires(chunk));
	}
	
	/**
	 * Evaluates and runs the text chunk if it is, in fact, a Lua Script. Things
	 * that are not Lua scripts include null and the empty string.
	 * @param	chunk			The text to interpret
	 * @return					The result of the evaluation, or null if none
	 */
	public LuaValue runIfExists(String chunk) {
		if (chunk == null || chunk.length() == 0) {
			return null;
		}
		return interpret(chunk).call();
	}
	
	/**
	 * Sets some context for the call, then runs the chunk if it exists. Does
	 * not run blanks and nulls. The caller will be set to the 'this' object
	 * in Lua, so then the Lua function can call functions on the caller.
	 * @param	chunk			The text to interpret
	 * @param	caller			The calling object
	 * @return					The result of the evaluation, or null if none
	 */
	public LuaValue run(String chunk, MapEvent caller) {
		globals.set("this", caller.toLua());
		return runIfExists(chunk);
	}
	
	/**
	 * Sets some context for this call, then evaluates it. The caller will be
	 * set to the 'this' object in Lua, so then the Lua function can call
	 * functions on the caller.
	 * @param	script			The script to interpret
	 * @param	caller			The calling object
	 * @return					THe result of the evaluation
	 */
	public LuaValue run(LuaValue script, LuaValue caller) {
		globals.set("this", caller);
		return script.call();
	}
	
	/**
	 * Generates a lua function for the caller object and sets it in the
	 * table. The function should be a getter or other small function with no
	 * arguments. Meant to be called as part of LuaConvertable conversions.
	 * @param	caller			The calling object undergoing conversion
	 * @param	table			The lua object to attach the function to
	 * @param	methodName		The name of the method to generate for
	 */
	public static void generateFunction(final Object caller, LuaValue table,
			final String methodName) {
		try {
			final Method method = caller.getClass().getMethod(methodName);
			LuaFunction func = new ZeroArgFunction() {
				@Override public LuaValue call() {
					try {
						return CoerceJavaToLua.coerce(method.invoke(caller));
					} catch (Exception e) {
						MGlobal.reporter.err("Lua invocation exception for :" +
								caller + " , method " + methodName, e);
						return LuaValue.NIL;
					}
				}
			};
			table.set(methodName, func);
		} catch (Exception e) {
			MGlobal.reporter.err("Bad method for class " + caller.getClass() +
					" with method " + methodName, e);
		}
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
