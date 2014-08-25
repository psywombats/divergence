/**
 *  PlayShake.java
 *  Created on Aug 4, 2014 3:09:39 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Shakes a player after damage.
 */
public class PlayShake extends PlaybackStep {
	
	protected ScreenBattle screen;
	protected Chara target;

	/**
	 * Creates a shake playback for a given player on a screen.
	 * @param	screen			The screen to play on
	 * @param	target			The target to shake
	 */
	public PlayShake(ScreenBattle screen, Chara target) {
		super(screen);
		this.screen = screen;
		this.target = target;
	}

	/**
	 * We immediately finish, even if the shake doesn't.
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return true;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.immediateShake(target);
	}

}
