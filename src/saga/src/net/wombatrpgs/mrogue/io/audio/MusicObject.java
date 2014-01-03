/**
 *  MusicObject.java
 *  Created on Mar 24, 2013 1:52:03 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io.audio;

import net.wombatrpgs.mrogueschema.audio.MusicMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Background music object. If it's playing, it's on a map.
 */
public class MusicObject extends AudioObject {
	
	protected MusicMDO mdo;
	protected Music coreMusic;
	
	protected float fadeTime;
	protected float previous;
	protected float targetVolume;
	protected float elapsed;

	/**
	 * Generate music for wherever. It'll automagically follow the hero.
	 * @param 	mdo				The data to generate music from
	 */
	public MusicObject(MusicMDO mdo) {
		super(mdo);
		this.mdo = mdo;
		
		fadeTime = 0;
		targetVolume = (float) mdo.volume / 100f;
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		coreMusic.dispose();
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
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
					coreMusic.setVolume((1f - (this.elapsed / fadeTime)) * previous + targetVolume * (this.elapsed / fadeTime));
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
		targetVolume = (float) mdo.volume / 100f;
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
	
	/** @return The volume */
	public float getVolume() {
		return coreMusic.getVolume();
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.audio.AudioObject#corePlay()
	 */
	@Override
	protected void corePlay() {
		this.targetVolume = mdo.volume;
		if (!coreMusic.isPlaying()) {
			coreMusic.play();
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.audio.AudioObject#coreStop()
	 */
	@Override
	protected void coreStop() {
		coreMusic.stop();
		targetVolume = 0;
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.audio.AudioObject#getLoaderClass()
	 */
	@Override
	protected Class<?> getLoaderClass() {
		return Music.class;
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.audio.AudioObject#postAudioProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	protected void postAudioProcessing(AssetManager manager) {
		coreMusic = (Music) manager.get(filename, getLoaderClass());
		coreMusic.setLooping(true);
		coreMusic.setVolume((float) mdo.volume / 100f);
	}

}
