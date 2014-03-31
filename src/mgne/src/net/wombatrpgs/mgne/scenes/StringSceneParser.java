/**
 *  StringSceneParser.java
 *  Created on Mar 31, 2014 12:56:51 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import net.wombatrpgs.mgne.core.MGlobal;

import org.luaj.vm2.LuaValue;

/**
 * Parses scenes based on a string, probably from a map event script.
 */
public class StringSceneParser extends SceneParser {
	
	protected String source;
	
	/**
	 * Creates a new string scene parser from a string representing lua code.
	 * Prefixes the proper library calls and stuff like that.
	 * @param	lua				The lua to run for the scene.
	 */
	public StringSceneParser(String lua) {
		this.source = lua;
		LuaValue script = MGlobal.lua.interpret(lua);
		commands = SceneLib.parseScene(script);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (source.length() > 30) {
			return "\"" + source.substring(0, 27) + "...\"";
		} else {
			return "\"" + source + "\"";
		}
	}

}
