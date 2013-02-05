/**
 *  SceneLoader.java
 *  Created on Feb 3, 2013 11:31:09 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io.loaders;

import net.wombatrpgs.rainfall.scenes.SceneData;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Kind of a janky thing, based on the MusicLoader in libgdx. Just sucks up a
 * text file and drops it into a container. Not well-commented because honestly
 * I don't know what's going on in here half the time.
 */
public class SceneLoader extends SynchronousAssetLoader<SceneData, SceneLoader.SceneParameter> {
	
	public SceneLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public SceneData load(AssetManager assetManager, String fileName, SceneParameter parameter) {
		return new SceneData(resolve(fileName));
	}

	@Override
	@SuppressWarnings("rawtypes") // it was like this in the original, okay?
	public Array<AssetDescriptor> getDependencies (String fileName, SceneParameter parameter) {
		return null;
	}
	
	static public class SceneParameter extends AssetLoaderParameters<SceneData> {
		
	}

}
