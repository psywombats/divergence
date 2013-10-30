/**
 *  AbilArmorPierce.java
 *  Created on Oct 18, 2013 4:17:33 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogueschema.characters.effects.AbilPoisonMDO;

/**
 * Deals damage like a magic attack.
 */
public class AbilPoison extends AbilEffect {
	
	protected AbilPoisonMDO mdo;
	
	/**
	 * Creates an effect given data, ability.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 */
	public AbilPoison(AbilPoisonMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct(java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			if (MGlobal.hero.inLoS(target.getParent())) {
				GameUnit.out().msg(target.getName() + " was poisoned.");
			}
			target.onAttackBy(actor.getUnit());
			
			final GameUnit victim = target;
			final int duration = mdo.durationMin + MGlobal.rand.nextInt(mdo.durationMax - mdo.durationMin);
			final Turnable t = new Turnable() {
				int elapsed = 0;
				@Override public void onTurn() {
					if (elapsed == duration) {
						victim.removeTurnChild(this);
						if (MGlobal.hero.inLoS(victim.getParent())) {
							MGlobal.ui.getNarrator().msg(victim.getName() +
									" recovered from poison.");
						}
						return;
					}
					int dmg = actor.getUnit().getStats().getMagDamage();
					dmg *= mdo.mult;
					dmg += mdo.base;
					dmg = victim.takeMagicDamage(dmg);
					victim.ensureAlive();
					if (MGlobal.hero.inLoS(victim.getParent())) {
						MGlobal.ui.getNarrator().msg(victim.getName() +
								" suffered " + dmg + " poison damage.");
					}
					elapsed += 1;
				}
			};
			target.addTurnChild(t);
		}
	}

}
