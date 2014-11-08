/**
 *  EmuBGM.java
 *  Created on Oct 13, 2014 9:14:53 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.audio.data.EmuMusicEntryMDO;

/**
 * Emulated background music held by a map.
 */
public class EmuBGM extends BackgroundMusic {
	
	protected EmuMusicEntryMDO mdo;
	
	/**
	 * Creates a new emu bgm to wrap a music object.
	 * @param	emuMusic		The music object to wrap
	 */
	public EmuBGM(EmuMusicEntryMDO mdo) {
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#playInternal()
	 */
	@Override
	protected void playInternal() {
		MGlobal.audio.playEmuBGM(mdo.refKey);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#stopInternal()
	 */
	@Override
	protected void stopInternal() {
		MGlobal.audio.fadeoutEmuBGM(0);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeInInternal(float)
	 */
	@Override
	protected void fadeInInternal(float seconds) {
		MGlobal.audio.playEmuBGM(mdo.refKey);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeOutInternal(float)
	 */
	@Override
	protected void fadeOutInternal(float seconds) {
		MGlobal.audio.fadeoutEmuBGM(Math.round(seconds));
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#disposeInternal()
	 */
	@Override
	protected void disposeInternal() {
		// nothing to do
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#getKey()
	 */
	@Override
	protected String getKey() {
		return mdo.refKey;
	}

}
