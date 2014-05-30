/**
 *  AbilPassiveMDO.java
 *  Created on Apr 2, 2014 11:22:59 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.stats.StatModMDO;

/**
 * Passive resistance or stat abilities.
 */
public class EffectPassiveMDO extends AbilEffectMDO {
	
	@InlineSchema(StatModMDO.class)
	public StatModMDO stats;

}
