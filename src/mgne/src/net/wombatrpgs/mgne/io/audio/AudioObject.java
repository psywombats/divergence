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
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgneschema.audio.data.AudioMDO;

/**
 * Superclass for both music and sound, handles loading LibGDX sounds. Replaces
 * the old extremely unhelpful class comment. Mostly load this from the sound
 * manager.
 */
public abstract class AudioObject implements	Disposable,
												Queueable,
												Updateable {
	
	protected AudioMDO mdo;
	protected String filename;
	
	/**
	 * Create a new sound from data.
	 * @param 	mdo				The data to create the sound from
	 */
	public AudioObject(AudioMDO mdo) {
		this.mdo = mdo;
		this.filename = Constants.AUDIO_DIR + mdo.file;
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
	 * Default does nothing
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// noop
	}

	/**
	 * The real legit play thing that handles both playing the sound and
	 * managing the map that we're a part of.
	 */
	public void play() {
		corePlay();
	}
	
	/**
	 * The counterpart to play. Handles stopping sound and mappy stuff.
	 */
	public void stop() {
		coreStop();
	}
	
	/**
	 * Start playing the underlying sound.
	 */
	protected abstract void corePlay();
	
	/**
	 * Stop playing the underlying sound.
	 */
	protected abstract void coreStop();
	
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
