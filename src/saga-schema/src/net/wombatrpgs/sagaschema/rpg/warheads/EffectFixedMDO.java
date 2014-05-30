/**
 *  AbilFixedMDO.java
 *  Created on Apr 2, 2014 12:58:50 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Deals fixed damage.
 */
public class EffectFixedMDO extends EffectCombatMDO {
	
	@Desc("Base damage")
	@DefaultValue("0")
	public Integer base;
	
	@Desc("Damage range - will deal up from base to base + range damage")
	@DefaultValue("0")
	public Integer range;
	
	@Desc("Defense stat - deducted from damage")
	@DefaultValue("DEF")
	@Nullable
	public Stat defenseStat;
	
	@Desc("Item accuracy - base chance to hit, usually 0-100")
	@DefaultValue("100")
	public Integer accuracy;
	
	@Desc("Accuracy stat - chance to hit increased by this")
	@DefaultValue("AGI")
	@Nullable
	public Stat accStat;
	
	@Desc("Dodge stat - chance to hit reduced by this")
	@DefaultValue("AGI")
	@Nullable
	public Stat dodgeStat;

}
