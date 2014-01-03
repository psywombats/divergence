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
import net.wombatrpgs.mrogueschema.characters.effects.AbilMagicDamageMDO;

/**
 * Deals damage like a magic attack.
 */
public class AbilMagicDamage extends AbilEffect {
	
	protected AbilMagicDamageMDO mdo;
	
	/**
	 * Creates an effect given data, ability.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 */
	public AbilMagicDamage(AbilMagicDamageMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct(java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			int dmg = actor.getUnit().getStats().getMagDamage();
			dmg *= mdo.mult;
			dmg += mdo.base;
			dmg = target.takeMagicDamage(dmg);
			if (MGlobal.hero.inLoS(target.getParent())) {
				if (dmg > 0) {
					GameUnit.out().msg(target.getName() + " took " + dmg + " magical damage.");
				} else {
					GameUnit.out().msg(target.getName() + " was unaffected.");
				}
			}
			target.onAttackBy(actor.getUnit());
			target.ensureAlive();
		}
	}

}
