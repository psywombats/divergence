/**
 *  LoadedBGM.java
 *  Created on Oct 12, 2014 1:37:04 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgneschema.audio.data.LoadedMusicEntryMDO;

/**
 * Countainer for a music object that meets the BGM interface.
 */
public class LoadedBGM extends BackgroundMusic {
	
	protected LoadedMusicEntryMDO mdo;
	protected MusicObject music;
	
	/**
	 * Creates a new loaded background music from an existing music object.
	 * @param	entryMDO		The music to use as the music source
	 */
	public LoadedBGM(LoadedMusicEntryMDO entryMDO) {
		this.mdo = entryMDO;
		music = new MusicObject(entryMDO);
		assets.add(music);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		music.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#playInternal()
	 */
	@Override
	protected void playInternal() {
		music.play();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#stopInternal()
	 */
	@Override
	protected void stopInternal() {
		music.stop();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeInInternal(float)
	 */
	@Override
	protected void fadeInInternal(float seconds) {
		music.fadeIn(seconds);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeOutInternal(float)
	 */
	@Override
	protected void fadeOutInternal(float seconds) {
		music.fadeOut(seconds);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#disposeInternal()
	 */
	@Override
	protected void disposeInternal() {
		music.dispose();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#getKey()
	 */
	@Override
	protected String getKey() {
		return music.getKey();
	}

}
