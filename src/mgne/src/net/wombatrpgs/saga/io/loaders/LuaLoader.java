/**
 *  LuaLoader.java
 *  Created on Jan 24, 2014 3:03:24 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.io.loaders;

import org.luaj.vm2.LuaValue;

import net.wombatrpgs.saga.core.SGlobal;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * I copy/pasted the scene loader and expected it to load lua scripts. Sort of.
 */
public class LuaLoader extends SynchronousAssetLoader<LuaValue, LuaLoader.LuaParameter> {

	public LuaLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	/**
	 * @see com.badlogic.gdx.assets.loaders.SynchronousAssetLoader#load
	 * (com.badlogic.gdx.assets.AssetManager, java.lang.String,
	 * com.badlogic.gdx.files.FileHandle, com.badlogic.gdx.assets.AssetLoaderParameters)
	 */
	@Override
	public LuaValue load(AssetManager assetManager, String fileName, FileHandle file, LuaParameter parameter) {
		return SGlobal.lua.load(resolve(fileName));
	}

	/**
	 * @see com.badlogic.gdx.assets.loaders.AssetLoader#getDependencies
	 * (java.lang.String, com.badlogic.gdx.files.FileHandle,
	 * com.badlogic.gdx.assets.AssetLoaderParameters)
	 */
	@SuppressWarnings("rawtypes") // dammit libgdx
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LuaParameter parameter) {
		return null;
	}

	static public class LuaParameter extends AssetLoaderParameters<LuaValue> {
		
	}

}
