/**
 *  Lua.java
 *  Created on Jan 24, 2014 1:22:57 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.files.FileHandle;

/**
 * Anything and everything lua. I don't know much about lua so this is going to
 * look a little empty for a bit.
 */
public class Lua {
	
	protected Globals globals;
	
	/**
	 * Creates and intializes the lua manager. This also sets up all required
	 * functions for use in lualand.
	 */
	public Lua() {
		globals = JsePlatform.standardGlobals();
	}
	
	/**
	 * Loads the contents of a file as a lua value.
	 * @param	file				The file to load, a lua source
	 * @return						The lua script in that file
	 */
	public LuaValue load(FileHandle file) {
		return globals.load(file.readString(), file.name());
	}

}
