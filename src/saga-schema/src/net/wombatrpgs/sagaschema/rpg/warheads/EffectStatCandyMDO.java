/**
 *  EffectStatCandyMDO.java
 *  Created on May 30, 2014 11:53:56 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Permanently raises a stat outside of battle.
 */
public class EffectStatCandyMDO extends AbilEffectMDO {
	
	@Desc("Stat")
	public Stat stat;
	
	@Desc("Min gain")
	public Integer minGain;
	
	@Desc("Max gain")
	public Integer maxGain;
	
	@Desc("Restricted races - only these races allowed to consume")
	public Race[] restrictRace;
	
	@Desc("Maximum effective value - characters with a stat value above this "
			+ "will receive no effect from this item, or 0 for no maximum")
	public Integer maxValue;

}
