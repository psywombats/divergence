/**
 *  AbilBoostMDO.java
 *  Created on Apr 1, 2014 3:51:58 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.data.Stat;

/**
 * Boosts a stat for the battle.
 */
public class AbilBoostMDO extends AbilEffectMDO {
	
	@Desc("Stats to boost - all will be affected equally")
	public Stat[] stat;
	
	@Desc("Base power - power of the boost before modifiers")
	public Integer power;
	
	@Desc("Power stat - this stat is doubled and added to base power")
	@Nullable
	public Stat powerStat;
	
	@Desc("Cap - boosted stat will never exceed this value, 0 for unlimited")
	public Integer cap;

}
