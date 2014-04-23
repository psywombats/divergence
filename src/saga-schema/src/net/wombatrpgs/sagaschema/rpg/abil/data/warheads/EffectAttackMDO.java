/**
 *  AbilBasic.java
 *  Created on Mar 31, 2014 9:16:48 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseFlag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Inflicts something on whoever, both damage and status.
 */
public class EffectAttackMDO extends AbilEffectMDO {
	
	@Desc("Projector")
	@DefaultValue("SINGLE_ENEMY")
	public OffenseProjector projector;
	
	@Desc("Damage type")
	@DefaultValue("PHYSICAL")
	public DamageType damType;
	
	@Desc("Base power - power of the attack before multipliers")
	@DefaultValue("0")
	public Integer power;
	
	@Desc("Attack stat - this stat is quartered and then multiplied by power")
	@DefaultValue("STR")
	@Nullable
	public Stat attackStat;
	
	@Desc("Defend stat - this stat is subtracted from incoming damage")
	@DefaultValue("DEF")
	@Nullable
	public Stat defendStat;
	
	@Desc("Other flags")
	public OffenseFlag[] sideEffects;

}
