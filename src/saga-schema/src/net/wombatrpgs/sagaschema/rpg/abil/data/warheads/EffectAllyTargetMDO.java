/**
 *  EffectAllyTargetingMDO.java
 *  Created on Apr 26, 2014 6:34:11 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AllyProjector;

/**
 * Any effect that targets an ally.
 */
public abstract class EffectAllyTargetMDO extends AbilEffectMDO {
	
	@Desc("Projector - what this ability covers")
	@DefaultValue("USER")
	public AllyProjector projector;

}
