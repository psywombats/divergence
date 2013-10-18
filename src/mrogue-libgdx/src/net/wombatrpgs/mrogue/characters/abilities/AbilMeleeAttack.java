/**
 *  AbilPhysicalDamage.java
 *  Created on Oct 18, 2013 4:48:21 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.abilities;

import java.util.List;

import net.wombatrpgs.mrogue.characters.GameUnit;
import net.wombatrpgs.mrogueschema.characters.effects.AbilMeleeAttackMDO;

/**
 * Like you walked into something.
 */
public class AbilMeleeAttack extends Effect {
	
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
	 * @see net.wombatrpgs.mrogue.characters.abilities.Effect#internalAct
	 * (java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			actor.getUnit().attack(target);
		}
	}

}
