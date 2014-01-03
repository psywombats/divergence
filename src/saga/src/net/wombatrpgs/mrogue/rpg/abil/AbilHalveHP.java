/**
 *  AbilHalveHP.java
 *  Created on Oct 18, 2013 9:13:28 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogueschema.characters.effects.AbilHalveHpMDO;

/**
 * Halves the HP of all targets, up to a max damage.
 */
public class AbilHalveHP extends AbilEffect {
	
	protected AbilHalveHpMDO mdo;

	/**
	 * Constructs an ability given data, parent.
	 * @param	mdo				The data to use to generate
	 * @param	abil			The parent to generate for
	 */
	public AbilHalveHP(AbilHalveHpMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct(java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit unit : targets) {
			int damage = (int) Math.floor(unit.getStats().hp * mdo.factor);
			if (damage > mdo.maxDamage) damage = mdo.maxDamage;
			if (MGlobal.hero.inLoS(unit.getParent())) {
				GameUnit.out().msg(unit.getName() + " suffered " + damage + " distortion damage.");
			}
			unit.takeRawDamage(damage);
			unit.onAttackBy(actor.getUnit());
		}
	}

}
