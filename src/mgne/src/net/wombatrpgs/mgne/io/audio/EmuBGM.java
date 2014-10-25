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
	 * Rounds seconds because the emulator doesn't support it.
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeInBGM(float)
	 */
	@Override
	public void fadeInBGM(float seconds) {
		MGlobal.audio.playEmuBGM(mdo.refKey);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeOutBGM(float)
	 */
	@Override
	public void fadeOutBGM(float seconds) {
		MGlobal.audio.fadeoutEmuBGM(Math.round(seconds));
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
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
