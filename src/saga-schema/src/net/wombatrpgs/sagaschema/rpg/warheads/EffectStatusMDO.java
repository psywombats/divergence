/**
 *  AbilStatusMDO.java
 *  Created on Apr 2, 2014 1:47:16 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.rpg.StatusMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Inflicts status conditions.
 */
public class EffectStatusMDO extends EffectEnemyTargetMDO {
	
	@Desc("Status to inflict")
	@SchemaLink(StatusMDO.class)
	public String status;
	
	@Desc("Accuracy - base chance to hit, from 0 to 100 usually")
	@DefaultValue("80")
	public Integer hit;
	
	@Desc("Accuracy stat - added to chance to hit")
	@DefaultValue("MANA")
	@Nullable
	public Stat accStat;
	
	@Desc("Evasion stat - subtracted from chance to hit")
	@DefaultValue("MANA")
	@Nullable
	public Stat evadeStat;
	
}
