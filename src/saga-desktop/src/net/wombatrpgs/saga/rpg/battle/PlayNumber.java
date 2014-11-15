/**
 *  NumberPlayback.java
 *  Created on Aug 4, 2014 11:46:38 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import java.util.List;

import net.wombatrpgs.saga.graphics.NumberPopup;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Number damage playback for when someone gets hit.
 */
public class PlayNumber extends PlaybackStep {
	
	protected NumberPopup popup;
	protected List<Chara> targets;

	/**
	 * Creates a number playback for some enemies.
	 * @param	screen			The screen to play on
	 * @param	damage			The amount of damage to take
	 * @param	targets			The list of targets being damaged
	 */
	public PlayNumber(ScreenBattle screen, int damage, List<Chara> targets) {
		super(screen);
		this.targets = targets;
		popup = new NumberPopup(damage);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return popup.isDone();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.immediateAnimate(popup, targets);
	}

}
