/**
 *  EmuLoader.java
 *  Created on Oct 11, 2014 11:55:05 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.loaders;

import gme.GbsEmu;
import gme.MusicEmu;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Loads up a gameboy emulator from file.
 */
public class EmuLoader extends SynchronousAssetLoader<MusicEmu, EmuLoader.EmuParameter> {
	
	public EmuLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	/**
	 * @see com.badlogic.gdx.assets.loaders.SynchronousAssetLoader#load
	 * (com.badlogic.gdx.assets.AssetManager, java.lang.String,
	 * com.badlogic.gdx.files.FileHandle, com.badlogic.gdx.assets.AssetLoaderParameters)
	 */
	@Override
	public MusicEmu load(AssetManager assetManager, String fileName,
			FileHandle file, EmuParameter parameter) {
		GbsEmu emu = new GbsEmu();
		emu.setSampleRate(44100);
		emu.loadFile(file.readBytes());
		return emu;
	}
	
	/**
	 * @see com.badlogic.gdx.assets.loaders.AssetLoader#getDependencies
	 * (java.lang.String, com.badlogic.gdx.files.FileHandle,
	 * com.badlogic.gdx.assets.AssetLoaderParameters)
	 */
	@SuppressWarnings("rawtypes") // dammit libgdx
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			EmuParameter parameter) {
		return null;
	}
	
	static public class EmuParameter extends AssetLoaderParameters<MusicEmu> {
		
	}

}
