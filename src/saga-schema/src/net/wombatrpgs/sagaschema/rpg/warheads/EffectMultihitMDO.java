/**
 *  AbilMultihit.java
 *  Created on Apr 1, 2014 2:23:53 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Multi-hit attack (2pincer, 3head, etc)
 */
public class EffectMultihitMDO extends EffectCombatMDO {
	
	@Desc("Base power - base multiplier of each hit")
	public Integer power;
	
	@Desc("Hits order - the attack will hit this many times with each use, " +
				"comma separated, like \"3,2,2,1,1\" and then cycle")
	public String hits;
	
	@Desc("Attack stat - this stat is quartered and then multiplied by power")
	@DefaultValue("STR")
	@Nullable
	public Stat attackStat;
	
	@Desc("Defend stat - this stat is subtracted from incoming damage")
	@DefaultValue("DEF")
	@Nullable
	public Stat defendStat;

}
