/**
 *  PlayMoveSprite.java
 *  Created on Sep 22, 2014 12:49:58 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Advance or retreat a hero doll.
 */
public class PlayMoveSprite extends PlaybackStep {
	
	/**
	 * Should the hero advance or return to normal?
	 */
	public enum SpriteMoveType {
		ADVANCE,
		RETURN,
	}
	
	protected Chara chara;
	protected SpriteMoveType type;

	/**
	 * Creates a new sprite movement for a hero.
	 * @param	screen			The battle screen to play on
	 * @param	hero			The hero to move
	 * @param	type			Whether to advance or step back
	 */
	public PlayMoveSprite(ScreenBattle screen, Chara hero, SpriteMoveType type) {
		super(screen);
		this.chara = hero;
		this.type = type;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return screen.isAdvanceFinished();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.immediateAdvance(chara, type);
	}

}
