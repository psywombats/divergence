/**
 *  BackgroundMusic.java
 *  Created on Oct 12, 2014 1:27:25 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;

/**
 * Bridges the gap between emulated and loaded background music.
 */
public abstract class BackgroundMusic extends AssetQueuer implements	Disposable,
																		Updateable {
	
	protected FinishListener listenerIn, listenerOut;
	protected float fadingTime;
	protected boolean fadingIn, fadingOut;
	protected boolean currentlyPlaying;
	protected boolean disposeWhenDone;
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (fadingIn || fadingOut) {
			fadingTime -= elapsed;
			if (fadingTime <= 0) {
				if (fadingIn) {
					fadingIn = false;
					if (listenerIn != null) {
						listenerIn.onFinish();
					}
				} else if (fadingOut) {
					fadingOut = false;
					if (listenerOut != null) {
						listenerOut.onFinish();
						listenerOut = null;
					}
					currentlyPlaying = false;
					if (disposeWhenDone) {
						disposeInternal();
					}
				}
			}
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (object.getClass() != this.getClass()) return false;
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
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public final void dispose() {
		disposeWhenDone = true;
		if (!currentlyPlaying) {
			disposeInternal();
		}
	}

	/**
	 * Checks if this background music should switch when this track starts
	 * playing on top of another track. For most bgm, this should just be a
	 * quick equals. The NoChangeBGM will want to always return false.
	 * @param	other		The other background music to compare against
	 * @return				True if the music should fade and switch
	 */
	public boolean shouldSwitchTo(BackgroundMusic other) {
		return !this.equals(other);
	}

	/**
	 * Fades in this music over a period of time. Should only be called from
	 * the sound manager.
	 * @param	seconds			The amount in seconds to fade in across
	 * @param	listener		The listener to notify when done, or null
	 */
	public final void fadeInBGM(float seconds, FinishListener listener) {
		this.listenerIn = listener;
		this.fadingTime = seconds;
		if (fadingIn) {
			MGlobal.reporter.warn("Fading in bgm twice");
		}
		if (fadingOut) {
			fadeOutInternal(0);
			fadingOut = false;
		}
		fadingIn = true;
		fadeInInternal(fadingTime);
		currentlyPlaying = true;
	}
	
	/**
	 * Fades out this music over a period of time. Should usually only be called
	 * from the sound manager.
	 * @param	seconds			The amount in seconds to fade out across
	 * @param	listener		The listener to notify when done, or null
	 */
	public final void fadeOutBGM(float seconds, FinishListener listener) {
		this.listenerOut = listener;
		this.fadingTime = seconds;
		if (fadingOut) {
			MGlobal.reporter.warn("Fading out bgm twice");
			return;
		}
		if (fadingIn) {
			fadeInInternal(0);
			fadingIn = false;
		}
		fadingOut = true;
		fadeOutInternal(fadingTime);
	}
	
	/**
	 * Immediately plays the BGM. Should only be called from the sound manager.
	 */
	public final void play() {
		fadingOut = false;
		if (fadingIn) {
			fadingIn = false;
			if (listenerIn != null) {
				listenerIn.onFinish();
			}
			return;
		}
		currentlyPlaying = true;
		playInternal();
	}
	
	/**
	 * Immediately stops the BGM. Should usually be called from sound manager.
	 */
	public final void stop() {
		if (!currentlyPlaying) {
			return;
		}
		fadingIn = false;
		if (fadingOut) {
			fadingOut = false;
			if (listenerOut != null) {
				listenerOut.onFinish();
			}
			return;
		}
		currentlyPlaying = false;
		stopInternal();
	}
	
	/**
	 * Returns the lookup key of this BGM. Two musics are the same if they have
	 * the same lookup key.
	 * @return					The ref key of this BGM
	 */
	protected abstract String getKey();
	
	/**
	 * Implementation for immediate play.
	 */
	protected abstract void playInternal();
	
	/**
	 * Implementation for immediate stop.
	 */
	protected abstract void stopInternal();
	
	/**
	 * Implementation for music fade-in.
	 * @param	seconds			The amount of time in seconds to fade across
	 */
	protected abstract void fadeInInternal(float seconds);
	
	/**
	 * Implementation for music fade-out.
	 * @param	seconds			The amount of time in seconds to fade across
	 */
	protected abstract void fadeOutInternal(float seconds);
	
	/**
	 * Dispose the underlying audio data. The bgm is guaranteed to not be on.
	 */
	protected abstract void disposeInternal();

}
