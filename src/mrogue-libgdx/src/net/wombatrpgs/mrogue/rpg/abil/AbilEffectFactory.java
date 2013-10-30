/**
 *  EffectFactory.java
 *  Created on Oct 18, 2013 4:51:36 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilArmorPierceMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilFixedDamageMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilHalveHpMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilMagicDamageMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilMeleeAttackMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilPhysicalDamageMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilPoisonMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilTeleportMDO;
import net.wombatrpgs.mrogueschema.characters.effects.AbilVampirismMDO;

/**
 * Another one of these shitty instanceof/getclass constructions for MDOs.
 */
public class AbilEffectFactory {
	
	/**
	 * Creates an effect given data key, parent.
	 * @param	mdoKey			The key to use to get generation data
	 * @param	abil			The ability to generate for
	 * @return
	 */
	public static AbilEffect createEffect(String mdoKey, Ability abil) {
		AbilityEffectMDO mdo = MGlobal.data.getEntryFor(mdoKey, AbilityEffectMDO.class);
		return createEffect(mdo, abil);
	}
	
	/**
	 * Creates an effect given data, parent.
	 * @param	mdo				The data to generate from
	 * @param	abil			The ability to generate for
	 * @return
	 */
	public static AbilEffect createEffect(AbilityEffectMDO mdo, Ability abil) {
		if (AbilMeleeAttackMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilMeleeAttack((AbilMeleeAttackMDO) mdo, abil);
		} else if (AbilVampirismMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilVampirism((AbilVampirismMDO) mdo, abil);
		} else if (AbilFixedDamageMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilFixedDamage((AbilFixedDamageMDO) mdo, abil);
		} else if (AbilPoisonMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilPoison((AbilPoisonMDO) mdo, abil);
		} else if (AbilTeleportMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilTeleport((AbilTeleportMDO) mdo, abil);
		} else if (AbilHalveHpMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilHalveHP((AbilHalveHpMDO) mdo, abil);
		} else if (AbilPhysicalDamageMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilPhysicalDamage((AbilPhysicalDamageMDO) mdo, abil);
		} else if (AbilMagicDamageMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilMagicDamage((AbilMagicDamageMDO) mdo, abil);
		} else if (AbilArmorPierceMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilArmorPierce((AbilArmorPierceMDO) mdo, abil);
		} else {
			MGlobal.reporter.err("Unknown ability type " + mdo);
			return null;
		}
	}

}
