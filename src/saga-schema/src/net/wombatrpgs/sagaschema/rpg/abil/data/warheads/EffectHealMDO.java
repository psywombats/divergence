/**
 *  AbilHealMDO.java
 *  Created on Apr 1, 2014 3:25:32 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.chara.data.Status;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Heals party.
 */
public class EffectHealMDO extends EffectAllyTargetMDO {
	
	@Desc("Heal base - base power for the heal, added to final value")
	@DefaultValue("0")
	public Integer base;
	
	@Desc("Heal power - multiplier for the heal")
	@DefaultValue("0")
	public Integer power;
	
	@Desc("Power stat - this stat is quartered and multiplied by power")
	@DefaultValue("None")
	@Nullable
	public Stat powerStat;

	@Desc("Heal status - cures these ailments")
	public Status[] status;

}
