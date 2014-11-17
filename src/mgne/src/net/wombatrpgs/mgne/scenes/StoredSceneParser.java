/**
 *  SceneParser.java
 *  Created on Feb 3, 2013 8:43:01 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import org.luaj.vm2.LuaValue;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;

/**
 * This thing takes a scene and then hijacks its parent level into doing its
 * bidding.
 * As of 2014-01-24, it plays back a series of Lua commands.
 */
public class StoredSceneParser extends SceneParser {
	
	protected String fileName;
	protected LuaValue caller;
	
	/**
	 * Creates a new scene parser for a given file. No autoplay. Assumes no
	 * repeat.
	 * @param	fileName		The filename to load, relative to scenes dir
	 * @param	caller			The lua value this for this script
	 */
	public StoredSceneParser(String fileName, LuaValue caller) {
		super();
		this.caller = caller;
		this.fileName = Constants.SCENES_DIR + fileName;
	}
	
	/**
	 * Creates a new scene parser for a given file with no caller.
	 * @param	fileName		The filename to laod, relative to scenes dir
	 */
	public StoredSceneParser(String fileName) {
		this(fileName, LuaValue.NIL);
	}
	
	/**
	 * Load the file if we're using one, otherwise we're anonymous and assume
	 * all the commands have been manually added.
	 * @see net.wombatrpgs.mgne.maps.MapThing#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		if (filename != null) {
			manager.load(filename, LuaValue.class);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		if (pass == 0) {
			LuaValue script = manager.get(filename, LuaValue.class);
			commands = SceneLib.parseScene(script, caller);
		}
		super.postProcessing(manager, pass);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (filename != null) {
			return filename;
		} else {
			return "anon scene";
		}
	}

}
