/**
 *  AbilStatusMDO.java
 *  Created on Apr 2, 2014 1:47:16 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;
import net.wombatrpgs.sagaschema.rpg.abil.data.StatusType;
import net.wombatrpgs.sagaschema.rpg.data.Stat;

/**
 * Inflicts status conditions.
 */
public class AbilStatusMDO extends AbilEffectMDO {
	
	@Desc("Projector")
	@DefaultValue("SINGLE_ENEMY")
	public OffenseProjector projector;
	
	@Desc("Status to inflict")
	public StatusType status;
	
	@Desc("Accuracy - base chance to hit, from 0 to 100 usually")
	@DefaultValue("80")
	public Integer hit;
	
	@Desc("Accuracy stat - added to chance to hit")
	@DefaultValue("MANA")
	public Stat accStat;
	
	@Desc("Evasion stat - subtracted from chance to hit")
	@DefaultValue("MANA")
	public Stat evadeStat;
	
}
