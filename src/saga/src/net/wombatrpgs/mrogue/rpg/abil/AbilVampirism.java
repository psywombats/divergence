/**
 *  AbilArmorPierce.java
 *  Created on Oct 18, 2013 4:17:33 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogueschema.characters.effects.AbilVampirismMDO;

/**
 * Deals damage like a magic attack.
 */
public class AbilVampirism extends AbilEffect {
	
	protected AbilVampirismMDO mdo;
	
	/**
	 * Creates an effect given data, ability.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 */
	public AbilVampirism(AbilVampirismMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct(java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			int dmg = 0;
			dmg += actor.getUnit().getStats().getDamage() * mdo.physMult;
			dmg += actor.getUnit().getStats().getMagDamage() * mdo.magMult;
			dmg += mdo.base;
			dmg = target.takeMagicDamage(dmg);
			if (MGlobal.hero.inLoS(target.getParent())) {
				if (dmg > 0) {
					GameUnit.out().msg(target.getName() + " took " + dmg + " damage.");
				} else {
					GameUnit.out().msg(target.getName() + " was unaffected.");
				}
			}
			int healt = actor.getUnit().heal((int) Math.ceil(dmg * mdo.healMult));
			if (MGlobal.hero.inLoS(actor)) {
				GameUnit.out().msg(actor.getUnit().getName() + " drained " + healt + " health.");
			}
			target.onAttackBy(actor.getUnit());
			target.ensureAlive();
		}
	}

}
