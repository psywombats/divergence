/**
 *  LoadableScript.java
 *  Created on 2014/07/17 21:42:51 for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.lua;

import org.luaj.vm2.LuaValue;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;

/**
 * A script that can load up things or something like that. Basically a LuaValue
 * that fits into the MAssets model.
 */
public class LoadableScript implements Queueable, Disposable {
	
	public String filename;
	public LuaValue value;
	
	/**
	 * Creates a new loadable script that loads from the given file.
	 * @param	filename		The name of the file to load, without directory
	 */
	public LoadableScript(String filename) {
		this.filename = Constants.SCENES_DIR + filename;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (net.wombatrpgs.mgne.core.MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(filename, LuaValue.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		if (pass == 0) {
			value = manager.get(filename, LuaValue.class);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		MGlobal.assets.unload(filename);
	}

	/**
	 * Returns the script after it's been loaded.
	 * @return					The loaded lua value contained in that script
	 */
	public LuaValue getScript() {
		if (value == null) {
			MGlobal.reporter.err("Script not loaded: " + filename);
		}
		return value;
	}

}
