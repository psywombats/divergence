/**
 *  PortraitAnimation.java
 *  Created on Aug 4, 2014 11:30:58 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.graphics.interfaces.PosRenderable;
import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Anything (battle animation or number popup) that can display over a battle
 * portrait and eventually finish.
 */
public abstract class PortraitAnim extends AssetQueuer implements	Updateable,
																	PosRenderable,
																	Disposable {
	
	protected float sinceStart;
	protected boolean started, playing;
	
	/**
	 * Creates a new animation. Blank.
	 */
	public PortraitAnim() {
		sinceStart = 0;
	}
	
	/** @return True if animation has started yet, false otherwise */
	public final boolean isPlaying() { return playing; }
	
	/** @return True if animation has at any point been playing */
	public final boolean isStarted() { return started; }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (playing) {
			sinceStart += elapsed;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// default is nothing
	}
	
	/**
	 * Check if this animation is done or not.
	 * @return					True if animation has finished, false if ongoing
	 */
	public abstract boolean isDone();
	
	/**
	 * Start and reset this animation.
	 * @param	screen			The screen this animation starts playing on
	 */
	public void start(ScreenBattle screen) {
		sinceStart = 0;
		started = true;
		playing = true;
	}
	
	/**
	 * Sets this animation back to its initial state.
	 */
	public void reset() {
		sinceStart = 0;
		playing = false;
	}
	
	/**
	 * Stops this animation from gaining time.
	 */
	public final void pause() {
		playing = false;
	}
	
	/**
	 * Pauses this animation at a certain point in time.
	 * @param time
	 */
	public final void pauseAt(float time) {
		pause();
		sinceStart = time;
	}
	
	/**
	 * Called by the screen when this animation is terminated.
	 * @param	screen			The screen this animation finished playing on
	 */
	public void finish(ScreenBattle screen) {
		playing = false;
	}

}
