/**
 *  EffectFactory.java
 *  Created on Oct 18, 2013 4:51:36 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.abilities;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilMeleeAttackMDO;

/**
 * Another one of these shitty instanceof/getclass constructions for MDOs.
 */
public class EffectFactory {
	
	/**
	 * Creates an effect given data key, parent.
	 * @param	mdoKey			The key to use to get generation data
	 * @param	abil			The ability to generate for
	 * @return
	 */
	public static Effect createEffect(String mdoKey, Ability abil) {
		AbilityEffectMDO mdo = MGlobal.data.getEntryFor(mdoKey, AbilityEffectMDO.class);
		return createEffect(mdo, abil);
	}
	
	/**
	 * Creates an effect given data, parent.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 * @return
	 */
	public static Effect createEffect(AbilityEffectMDO mdo, Ability abil) {
		if (AbilMeleeAttackMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilMeleeAttack((AbilMeleeAttackMDO) mdo, abil);
		} else {
			MGlobal.reporter.err("Unknown ability type " + mdo);
			return null;
		}
	}

}
