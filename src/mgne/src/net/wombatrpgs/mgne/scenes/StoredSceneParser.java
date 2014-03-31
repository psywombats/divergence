/**
 *  SceneParser.java
 *  Created on Feb 3, 2013 8:43:01 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import org.luaj.vm2.LuaValue;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.Constants;

/**
 * This thing takes a scene and then hijacks its parent level into doing its
 * bidding.
 * As of 2014-01-24, it plays back a series of Lua commands.
 */
public class StoredSceneParser extends SceneParser {
	
	protected String filename;
	
	/**
	 * Creates a new scene parser for a given file. No autoplay. Assumes no
	 * repeat.
	 * @param	fileName		The filename to load, relative to scenes dir
	 */
	public StoredSceneParser(String filename) {
		super();
		this.filename = Constants.SCENES_DIR + filename;
	}
	
	/**
	 * Load the file if we're using one, otherwise we're anonymous and assume
	 * all the commands have been manually added.
	 * @see net.wombatrpgs.mgne.maps.MapThing#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (filename != null) {
			manager.load(filename, LuaValue.class);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (pass == 0) {
			LuaValue script = manager.get(filename, LuaValue.class);
			commands = SceneLib.parseScene(script);
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
