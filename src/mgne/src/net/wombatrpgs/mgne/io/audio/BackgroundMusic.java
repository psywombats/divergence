/**
 *  BackgroundMusic.java
 *  Created on Oct 12, 2014 1:27:25 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;

/**
 * Bridges the gap between emulated and loaded background music.
 */
public abstract class BackgroundMusic extends AssetQueuer implements Disposable {

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object.getClass() != this.getClass()) {
			return false;
		}
		BackgroundMusic bgm = (BackgroundMusic) object;
		return getKey().equals(bgm.getKey());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}
	
	/**
	 * Immediately starts playing this background music.
	 */
	public void playBGM() {
		fadeInBGM(0);
	}

	/**
	 * Fades in this music over a period of time.
	 * @param	seconds			The amount in seconds to fade in across
	 */
	public abstract void fadeInBGM(float seconds);
	
	/**
	 * Fades out this music over a period of time.
	 * @param	seconds			The amount in seconds to fade out across
	 */
	public abstract void fadeOutBGM(float seconds);
	
	/**
	 * Returns the lookup key of this BGM. Two musics are the same if they have
	 * the same lookup key.
	 * @return					The ref key of this BGM
	 */
	protected abstract String getKey();

}
