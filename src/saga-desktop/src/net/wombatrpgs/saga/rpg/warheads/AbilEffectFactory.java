/**
 *  ItemEffectFactory.java
 *  Created on Apr 23, 2014 3:06:42 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectAttackMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectBoostMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectDefendMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectFixedMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectHealMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectMultihitMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectNothingMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectPassiveMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectStatCandyMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectStatusMDO;

/**
 * Constructs the item effect for combat items based on their MDO.
 */
public class AbilEffectFactory {
	
	/**
	 * Factory method. Creates abil effects from mdo for parents.
	 * @param	mdo				The mdo to create from
	 * @param	item			The item to create for
	 * @return					The newly created abil effect
	 */
	public static AbilEffect create(AbilEffectMDO mdo, CombatItem item) {
		if (EffectAttackMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectAttack((EffectAttackMDO) mdo, item);
		} else if (EffectNothingMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectNothing((EffectNothingMDO) mdo, item);
		} else if (EffectPassiveMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectPassive((EffectPassiveMDO) mdo, item);
		} else if (EffectFixedMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectFixed((EffectFixedMDO) mdo, item);
		} else if (EffectBoostMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectBoost((EffectBoostMDO) mdo, item);
		} else if (EffectDefendMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectDefend((EffectDefendMDO) mdo, item);
		} else if (EffectHealMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectHeal((EffectHealMDO) mdo, item);
		} else if (EffectMultihitMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectMultihit((EffectMultihitMDO) mdo, item);
		} else if (EffectStatusMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectStatus((EffectStatusMDO) mdo, item);
		} else if (EffectStatCandyMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectStatCandy((EffectStatCandyMDO) mdo, item);
		} else {
			MGlobal.reporter.err("Unimplemented abil effect type: " +
					mdo.getClass());
			return null;
		}
	}
	
	/**
	 * Factory method. Creates abil effect from key to data mdo.
	 * @param	key				The key of the mdo to create from
	 * @param	item			The item to create for
	 * @return					The newly created abil effect
	 */
	public static AbilEffect create(String key, CombatItem item) {
		return create(MGlobal.data.getEntryFor(key, AbilEffectMDO.class), item);
	}

}
