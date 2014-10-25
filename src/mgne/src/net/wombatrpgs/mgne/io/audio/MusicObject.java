/**
 *  MusicObject.java
 *  Created on Mar 24, 2013 1:52:03 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.audio.data.LoadedMusicEntryMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Preloaded background music, usually MP3 or OGG etc.
 */
public class MusicObject extends LoadedAudioObject {
	
	protected LoadedMusicEntryMDO mdo;
	protected transient Music coreMusic;
	
	protected float fadeTime;
	protected float previous;
	protected float targetVolume;
	protected float elapsed;

	/**
	 * Generate music for wherever. It'll automagically follow the hero.
	 * @param 	mdo				The data to generate music from
	 */
	public MusicObject(LoadedMusicEntryMDO mdo) {
		super(mdo.path);
		this.mdo = mdo;
		
		fadeTime = 0;
		targetVolume = 100;
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
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		this.elapsed += elapsed;
		if (fadeTime != 0) {
			if (this.elapsed >= fadeTime) {
				this.elapsed = fadeTime;
			}
			if (coreMusic.getVolume() < targetVolume) {
				coreMusic.setVolume(targetVolume * this.elapsed / fadeTime);
			} else {
				if (this.elapsed >= fadeTime) {
					coreStop();
				} else {
					coreMusic.setVolume((1f - (this.elapsed / fadeTime)) * previous + 
							targetVolume * (this.elapsed / fadeTime));
				}
			}
			if (this.elapsed == fadeTime) {
				fadeTime = 0;
			}
		} else {
			if (!coreMusic.isPlaying()) { coreMusic.play(); }
			coreMusic.setVolume(targetVolume);
		}
	}

	/**
	 * Pauses music playback for things like losing screen focus or whatever.
	 */
	public void pause() {
		coreMusic.pause();
	}
	
	/**
	 * Determines if two musics are actually -- the same thing!!
	 * @param 	object				The other music object
	 * @return						True if they match, false otherwise
	 */
	public boolean matches(MusicObject object) {
		if (object == null) return false;
		return this.mdo == object.mdo;
	}
	
	/**
	 * Gets the comparison key of this object. Two music objects are the same if
	 * they share a key.
	 * @return					The unique key of this music
	 */
	public String getKey() {
		return mdo.refKey;
	}
	
	/**
	 * Sets this music to fade in in a certain duration. Starts playing if not
	 * already playing.
	 * @param	time			How long it should take to fade in (in s)
	 */
	public void fadeIn(float time) {
		if (!coreMusic.isPlaying()) {
			corePlay();
		}
		this.fadeTime = time;
		elapsed = 0;
		targetVolume = 100;
	}
	
	/**
	 * Sets this music to fade out in a certain duration. Stops playing when
	 * finished.
	 * @param	time			How long it should take to fade out (in s)
	 */
	public void fadeOut(float time) {
		this.fadeTime = time;
		elapsed = 0;
		targetVolume = 0;
		previous = coreMusic.getVolume();
	}
	
	/** @return The volume, from 0-100 */
	public float getVolume() { return coreMusic.getVolume(); }

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#corePlay()
	 */
	@Override
	protected void corePlay() {
		this.targetVolume = 100;
		if (!coreMusic.isPlaying()) {
			coreMusic.play();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#coreStop()
	 */
	@Override
	protected void coreStop() {
		coreMusic.stop();
		targetVolume = 0;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#getLoaderClass()
	 */
	@Override
	protected Class<?> getLoaderClass() {
		return Music.class;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.LoadedAudioObject#postAudioProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	protected void postAudioProcessing(AssetManager manager) {
		coreMusic = (Music) manager.get(filename, getLoaderClass());
		coreMusic.setLooping(true);
		coreMusic.setVolume(100);
	}

}
