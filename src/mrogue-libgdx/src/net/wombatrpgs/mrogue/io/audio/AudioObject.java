/**
 *  Sound.java
 *  Created on Mar 21, 2013 8:52:59 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io.audio;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.graphics.Disposable;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogueschema.audio.data.AudioMDO;

/**
 * Any old sound that can be played and stopped at will. ...well sort of? Maybe?
 * Honestly 90% of the time I write these class descriptions before I actually
 * write the class. AAND yep it just turned abstract. Shouldn't write these so
 * fast. Actually now it's a factory for libgdx sounds.
 */
public abstract class AudioObject extends MapThing implements Disposable {
	
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
	 * @see net.wombatrpgs.mrogue.maps.MapThing#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		manager.load(filename, getLoaderClass());
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		postAudioProcessing(manager);
	}
	
	/**
	 * The real legit play thing that handles both playing the sound and
	 * managing the map that we're a part of.
	 */
	public void play() {
//		if (getLevel() != parent.getLevel()) {
//			if (getLevel() != null) {
//				getLevel().removeObject(this);
//			}
//			parent.getLevel().addObject(this);
//		}
		corePlay();
	}
	
	/**
	 * The counterpart to play. Handles stopping sound and mappy stuff.
	 */
	public void stop() {
		coreStop();
		if (getParent() != null) {
			getParent().removeObject(this);
		}
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
