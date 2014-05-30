/**
 *  PlaybackStep.java
 *  Created on May 23, 2014 9:56:59 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Either a text printout or battle animation that causes battle playback to
 * pause until the playback is over.
 */
public abstract class PlaybackStep {
	
	protected ScreenBattle screen;
	
	protected boolean started;
	
	/**
	 * Creates a new playback step for a given battle screen
	 * @param	screen			The screen to create for
	 */
	public PlaybackStep(ScreenBattle screen) {
		this.screen = screen;
		started = false;
	}
	
	/** @return True if this step has started playing yet */
	public boolean isStarted() { return started; }
	
	/**
	 * Starts playback by setting an internal flag, then calling the subclass.
	 */
	public void start() {
		started = true;
		internalStart();
	}
	
	/**
	 * Check if this playback has completed.
	 * @return					True if playback is complete, false if ongoing
	 */
	public abstract boolean isDone();

	/**
	 * Starts playback by instructing the battle to do something.
	 */
	public abstract void internalStart();
}
