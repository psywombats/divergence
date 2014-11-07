/**
 *  NoChangeBGM.java
 *  Created on Nov 6, 2014 11:20:01 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.MGlobal;

/**
 * Indicates that background music should not change on the current map.
 */
public class NoChangeBGM extends BackgroundMusic {
	
	public static String NO_CHANGE_KEY = "no_change";

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// nothing to do
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeInBGM(float)
	 */
	@Override
	public void fadeInBGM(float seconds) {
		MGlobal.reporter.warn("No change BGM asked to fade in");
	}

	/**
	 * @see net.wombatrpgs.mgne.io.audio.BackgroundMusic#fadeOutBGM(float)
	 */
	@Override
	public void fadeOutBGM(float seconds) {
		MGlobal.reporter.warn("No change BGM asked to fade out");
	}

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

}
