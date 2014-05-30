/**
 *  EffectEnemyTargetMDO.java
 *  Created on Apr 28, 2014 6:26:28 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;

/**
 * Superclass for effects that target the enemy.
 */
public abstract class EffectEnemyTargetMDO extends AbilEffectMDO {
	
	@Desc("Projector")
	@DefaultValue("SINGLE_ENEMY")
	public OffenseProjector projector;

}
