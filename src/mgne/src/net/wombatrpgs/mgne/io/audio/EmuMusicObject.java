/**
 *  EmuMusicObject.java
 *  Created on Oct 12, 2014 1:04:37 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.audio.data.EmuMusicEntryMDO;

/**
 * An emulated track of background music, put into the audio object hierarchy.
 */
public class EmuMusicObject extends AudioObject {
	
	protected EmuMusicEntryMDO mdo;
	
	/**
	 * Creates a new emulated music object from data.
	 * @param	mdo				The data to create from.
	 */
	public EmuMusicObject(EmuMusicEntryMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Two EmuMusicObjects match if they have the same key.
	 * @return					The identifier key of this track
	 */
	public String getKey() {
		return mdo.refKey;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.AudioObject#corePlay()
	 */
	@Override
	protected void corePlay() {
		MGlobal.audio.playEmuBGM(mdo.refKey);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.AudioObject#coreStop()
	 */
	@Override
	protected void coreStop() {
		MGlobal.audio.fadeoutEmuBGM(0);
	}

}
