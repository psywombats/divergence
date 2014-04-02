/**
 *  AbilPassiveMDO.java
 *  Created on Apr 2, 2014 11:22:59 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.StatusType;
import net.wombatrpgs.sagaschema.rpg.data.StatSetMDO;

/**
 * Passive resistance or stat abilities.
 */
public class AbilPassiveMDO extends AbilEffectMDO {
	
	@Desc("Damage type resistances")
	public DamageType[] damResist;
	
	@Desc("Status effect resistances")
	public StatusType[] statusResist;
	
	@Desc("Damage type weaknesses")
	public DamageType[] damWeak;
	
	@Desc("Status effect weaknesses")
	public StatusType[] statusWeak;
	
	@InlineSchema(StatSetMDO.class)
	public StatSetMDO stats;

}
