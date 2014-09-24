/**
 *  PlayDeath.java
 *  Created on Sep 24, 2014 12:12:16 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Animates an enemy biting it.
 */
public class PlayDeath extends PlaybackStep {
	
	protected int index;

	/**
	 * Creates a new death animation for an enemy.
	 * @param	screen			The screen to animate on
	 * @param	index			The group of the enemy that's dying
	 */
	public PlayDeath(ScreenBattle screen, int index) {
		super(screen);
		this.index = index;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return screen.isDeathFinished();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.immediateAnimateDeath(index);
	}

}
