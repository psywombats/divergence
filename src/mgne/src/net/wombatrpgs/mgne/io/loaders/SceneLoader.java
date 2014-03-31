/**
 *  SceneLoader.java
 *  Created on Feb 3, 2013 11:31:09 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.loaders;

import net.wombatrpgs.mgne.scenes.LineData;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Kind of a janky thing, based on the MusicLoader in libgdx. Just sucks up a
 * text file and drops it into a container. Not well-commented because honestly
 * I don't know what's going on in here half the time.
 */
public class SceneLoader extends SynchronousAssetLoader<LineData, SceneLoader.SceneParameter> {
	
	/**
	 * @see com.badlogic.gdx.assets.loaders.SynchronousAssetLoader#load
	 * (com.badlogic.gdx.assets.AssetManager, java.lang.String,
	 * com.badlogic.gdx.files.FileHandle, com.badlogic.gdx.assets.AssetLoaderParameters)
	 */
	@Override
	public LineData load(AssetManager assetManager, String fileName,
			FileHandle file, SceneParameter parameter) {
		return new LineData(resolve(fileName));
	}

	/**
	 * @see com.badlogic.gdx.assets.loaders.AssetLoader#getDependencies
	 * (java.lang.String, com.badlogic.gdx.files.FileHandle,
	 * com.badlogic.gdx.assets.AssetLoaderParameters)
	 */
	@SuppressWarnings("rawtypes") // another libgdx weird thing
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, SceneParameter parameter) {
		return null;
	}

	public SceneLoader (FileHandleResolver resolver) {
		super(resolver);
	}
	
	static public class SceneParameter extends AssetLoaderParameters<LineData> {
		
	}

}
