/**
 *  AbilBasic.java
 *  Created on Mar 31, 2014 9:16:48 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.MissType;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Inflicts something on whoever, just damage and no status.
 */
public class EffectAttackMDO extends EffectCombatMDO {
	
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
	
	@Desc("Miss type - generally only melee attacks should miss")
	@DefaultValue("CAN_MISS")
	public MissType miss;

}
