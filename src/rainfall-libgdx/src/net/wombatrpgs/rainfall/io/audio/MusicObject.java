/**
 *  MusicObject.java
 *  Created on Mar 24, 2013 1:52:03 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io.audio;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.audio.MusicMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Background music object. If it's playing, it's on a map.
 */
public class MusicObject extends AudioObject {
	
	protected MusicMDO mdo;
	protected Music coreMusic;

	/**
	 * Generate music for wherever. It'll automagically follow the hero.
	 * @param 	mdo				The data to generate music from
	 */
	public MusicObject(MusicMDO mdo) {
		super(mdo, RGlobal.hero);
		this.mdo = mdo;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		coreMusic.dispose();
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
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#corePlay()
	 */
	@Override
	protected void corePlay() {
		if (!coreMusic.isPlaying()) {
			coreMusic.play();
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#coreStop()
	 */
	@Override
	protected void coreStop() {
		coreMusic.stop();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#getLoaderClass()
	 */
	@Override
	protected Class<?> getLoaderClass() {
		return Music.class;
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#postAudioProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	protected void postAudioProcessing(AssetManager manager) {
		coreMusic = (Music) manager.get(filename, getLoaderClass());
		coreMusic.setLooping(true);
		coreMusic.setVolume((float) mdo.volume / 100.f);
	}

}
