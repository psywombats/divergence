/**
 *  AnimPlayback.java
 *  Created on May 23, 2014 10:29:35 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.List;

import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.screen.ScreenBattle;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimMDO;

/**
 * Plays back a battle animation.
 */
public class AnimPlayback extends PlaybackStep {
	
	protected BattleAnimMDO animMDO;
	protected List<Chara> targets;

	/**
	 * Creates a playback step for a given screen and animation with targets.
	 * @param	screen			The screen running the battle
	 * @param	animMDO			The MDO of the animation to play
	 * @param	targets			The targets of the animation
	 */
	public AnimPlayback(ScreenBattle screen, BattleAnimMDO animMDO, List<Chara> targets) {
		super(screen);
		this.animMDO = animMDO;
		this.targets = targets;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return screen.isAnimFinished();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.immediateAnimate(animMDO, targets);
	}

}
