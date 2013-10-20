/**
 *  AbilPhysicalDamage.java
 *  Created on Oct 18, 2013 4:48:21 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.List;

import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.travel.Step;
import net.wombatrpgs.mrogueschema.characters.effects.AbilMeleeAttackMDO;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * Like you walked into something.
 */
public class AbilMeleeAttack extends AbilEffect {
	
	protected AbilMeleeAttackMDO mdo;

	/**
	 * Constructs a new effect given data, parent
	 * @param	mdo				The data to generate for
	 * @param	abil			The ability to generate for
	 */
	public AbilMeleeAttack(AbilMeleeAttackMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct
	 * (java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			actor.getUnit().attack(target);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#getStep()
	 */
	@Override
	public Step getStep() {
		int start = 0;
		final int fstart;
		for (int i = 0; i < OrthoDir.values().length; i += 1) {
			if (actor.getFacing() == OrthoDir.values()[i]) {
				start = i;
				break;
			}
		}
		fstart = start;
		return new Step(abil.getActor()) {
			@Override public void update(float elapsed) {
				super.update(elapsed);
				int step = (int) Math.floor(totalElapsed * (float)OrthoDir.values().length / allotted);
				actor.setFacing(OrthoDir.values()[(step+fstart) % OrthoDir.values().length]);
			}
			@Override public void onEnd() {
				super.onEnd();
				actor.setFacing(OrthoDir.values()[fstart]);
			}
		};
	}

}
