/**
 *  Sound.java
 *  Created on Mar 21, 2013 8:52:59 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;

/**
 * A loaded sound effect or music file, as opposed to the generated PCM audio
 * from custom sound effects or emulated music.
 */
public abstract class LoadedAudioObject extends AudioObject {
	
	protected String filename;
	
	/**
	 * Create a new sound from data.
	 * @param	filename		The name of the sound file to load from
	 */
	public LoadedAudioObject(String filename) {
		this.filename = Constants.AUDIO_DIR + filename;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(filename, getLoaderClass());
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		postAudioProcessing(manager);
	}
	
	/**
	 * This is the underlying libgdx audio type that will be queued/loaded.
	 * @return					The libgdx audio type
	 */
	protected abstract Class<?> getLoaderClass();
	
	/**
	 * Responsible for loading up the core audio. Called during normal post
	 * processing. By "loading up" that's just setting some field.
	 * @param	manager			The manager to load from
	 */
	protected abstract void postAudioProcessing(AssetManager manager);

}
