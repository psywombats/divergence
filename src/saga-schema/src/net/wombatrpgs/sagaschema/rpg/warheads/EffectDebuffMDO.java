/**
 *  EffectDebuffMDO.java
 *  Created on Jul 2, 2014 11:57:28 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Deducts a stat from the enemy
 */
public class EffectDebuffMDO extends EffectEnemyTargetMDO {
	
	@Desc("Base power - power of the debuff before multipliers")
	@DefaultValue("0")
	public Integer power;
	
	@Desc("Affected stat - the stat to drain from the enemy")
	@DefaultValue("STR")
	public Stat drainStat;
	
	@Desc("Attack stat - this stat is quartered and then multiplied by power")
	@DefaultValue("MANA")
	@Nullable
	public Stat attackStat;
	
	@Desc("Defend stat - this stat is subtracted from incoming stat drain")
	@DefaultValue("MANA")
	@Nullable
	public Stat defendStat;

}
