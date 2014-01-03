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
import net.wombatrpgs.mrogueschema.characters.effects.AbilArmorPierceMDO;

/**
 * Most spell damage falls into this category.
 */
public class AbilArmorPierce extends AbilEffect {
	
	protected AbilArmorPierceMDO mdo;
	
	/**
	 * Creates an effect given data, ability.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 */
	public AbilArmorPierce(AbilArmorPierceMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct(java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			int dmg = actor.getUnit().getStats().getDamage();
			dmg -= Math.floor((float) target.getStats().armor * (1f-mdo.pierce));
			target.takeRawDamage(dmg);
			if (MGlobal.hero.inLoS(target.getParent())) {
				GameUnit.out().msg(target.getName() + " took " + dmg + " damage.");
			}
			target.ensureAlive();
		}
	}

}
