/**
 *  NoChangeBGM.java
 *  Created on Nov 6, 2014 11:20:01 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

/**
 * Indicates that background music should not change on the current map.
 */
public class NoChangeBGM extends BackgroundMusic {
	
	public static String NO_CHANGE_KEY = "no_change";

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#shouldSwitchTo
	 * (net.wombatrpgs.mgne.io.audio.BackgroundMusic)
	 */
	@Override
	public boolean shouldSwitchTo(BackgroundMusic other) {
		return false;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#getKey()
	 */
	@Override
	protected String getKey() {
		return NO_CHANGE_KEY;
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeInInternal(float)
	 */
	@Override
	protected void fadeInInternal(float seconds) {
		// no need to do anything
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeOutInternal(float)
	 */
	@Override
	protected void fadeOutInternal(float seconds) {
		// no need to do anything
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#playInternal()
	 */
	@Override
	protected void playInternal() {
		// no need to do anything
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#stopInternal()
	 */
	@Override
	protected void stopInternal() {
		// no need to do anything
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#disposeInternal()
	 */
	@Override
	protected void disposeInternal() {
		// no need to do anything
	}

}
