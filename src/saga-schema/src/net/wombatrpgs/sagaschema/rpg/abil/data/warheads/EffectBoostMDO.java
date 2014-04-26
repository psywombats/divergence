/**
 *  AbilBoostMDO.java
 *  Created on Apr 1, 2014 3:51:58 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Boosts a stat for the battle.
 */
public class EffectBoostMDO extends EffectAllyTargetMDO {
	
	@Desc("Stat to boost")
	@DefaultValue("STR")
	public Stat stat;
	
	@Desc("Base power - power of the boost before modifiers")
	@DefaultValue("0")
	public Integer power;
	
	@Desc("Power stat - this stat is added to base power")
	@DefaultValue("None")
	@Nullable
	public Stat powerStat;
	
	@Desc("Cap - boosted stat will never exceed this value, 0 for unlimited")
	public Integer cap;

}
