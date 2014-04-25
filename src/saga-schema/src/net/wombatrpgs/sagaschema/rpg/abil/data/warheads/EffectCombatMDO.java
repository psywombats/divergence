/**
 *  EffectCombatMDO.java
 *  Created on Apr 25, 2014 11:34:24 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseFlag;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;

/**
 * Superclass for some common combat data.
 */
public class EffectCombatMDO extends AbilEffectMDO {
	
	@Desc("Projector")
	@DefaultValue("SINGLE_ENEMY")
	public OffenseProjector projector;
	
	@Desc("Damage type")
	@DefaultValue("PHYSICAL")
	public DamageType damType;
	
	@Desc("Other flags")
	public OffenseFlag[] sideEffects;

}
