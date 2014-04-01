/**
 *  AbilHealMDO.java
 *  Created on Apr 1, 2014 3:25:32 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.HealProjector;
import net.wombatrpgs.sagaschema.rpg.abil.data.HealType;
import net.wombatrpgs.sagaschema.rpg.data.Stat;

/**
 * Heals party.
 */
public class AbilHealMDO extends AbilEffectMDO {
	
	@Desc("Projector - who this ability heals")
	public HealProjector projector;
	
	@Desc("Heal type - will heal all of the following")
	public HealType[] heals;
	
	@Desc("Base power - power of the heal before modifiers")
	public Integer power;
	
	@Desc("Power stat - this stat is doubled and added to base power")
	@Nullable
	public Stat powerStat;

}
