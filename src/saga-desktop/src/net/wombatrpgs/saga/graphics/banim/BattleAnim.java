/**
 *  BattleAnimation.java
 *  Created on May 23, 2014 8:40:10 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics.banim;

import net.wombatrpgs.mgne.io.audio.SoundObject;
import net.wombatrpgs.saga.graphics.PortraitAnim;
import net.wombatrpgs.saga.screen.ScreenBattle;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleAnimMDO;

/**
 * A thing that can be played back for an attack during battle.
 */
public abstract class BattleAnim extends PortraitAnim {
	
	protected BattleAnimMDO mdo;
	
	protected SoundObject sound;
	
	/**
	 * Creates a new battle anim from data.
	 * @param	mdo				The data to create from.
	 */
	public BattleAnim(BattleAnimMDO mdo) {
		this.mdo = mdo;
		if (mdo.sound != null) {
			sound = SoundObject.createFromFile(mdo.sound);
			assets.add(sound);
		}
	}

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return 0; }

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return 0; }
	
	/**
	 * Plays the sound associated with this animation. Called at the beginning
	 * of the animation and when used on the map.
	 */
	public void playSound() {
		if (sound != null) {
			sound.play();
		}
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.PortraitAnim#start
	 * (net.wombatrpgs.saga.screen.ScreenBattle)
	 */
	@Override
	public void start(ScreenBattle screen) {
		super.start(screen);
		playSound();
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.PortraitAnim#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (sound != null) {
			sound.dispose();
		}
	}

}
