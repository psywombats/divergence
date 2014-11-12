/**
 *  SoundObject.java
 *  Created on Mar 24, 2013 1:42:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.audio.data.SoundEffectEntryMDO;

/**
 * A sound effect map object.
 */
public class SoundObject extends LoadedAudioObject {
	
	protected transient Sound coreSound;

	/**
	 * Constructs a sound object for another object from data.
	 * @param	mdo				The data to create from
	 */
	public SoundObject(SoundEffectEntryMDO mdo) {
		super(mdo.file);
	}
	
	/**
	 * Constructs a sound object for a given sound file.
	 * @param	filename		The name of the data file, relative to audio dir
	 */
	public SoundObject(String filename) {
		super(filename);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		if (MGlobal.assets.isLoaded(filename)) {
			MGlobal.assets.unload(filename);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#corePlay()
	 */
	@Override
	protected void corePlay() {
		long id = coreSound.play(100);
		coreSound.setLooping(id, false);
	}

	/**
	 * Note this stops all playing instances.
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#coreStop()
	 */
	@Override
	protected void coreStop() {
		coreSound.stop();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#getLoaderClass()
	 */
	@Override
	protected Class<?> getLoaderClass() {
		return Sound.class;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#postAudioProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	protected void postAudioProcessing(AssetManager manager) {
		coreSound = (Sound) manager.get(filename, getLoaderClass());
	}

}
