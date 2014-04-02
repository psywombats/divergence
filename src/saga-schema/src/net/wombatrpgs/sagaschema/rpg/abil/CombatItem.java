/**
 *  AbilityMDO.java
 *  Created on Feb 24, 2014 7:30:23 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.InlinePolymorphic;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.PolymorphicLink;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;

/**
 * MDO for actions.
 */
@Path("rpg/")
public class CombatItem extends MainSchema {
	
	@Desc("Ability name - displayed in-game")
	public String abilityName;
	
	@Desc("Uses - or zero for infinite uses")
	@DefaultValue("0")
	public Integer uses;
	
	@Desc("Targeting type")
	@DefaultValue("SINGLE_ENEMY")
	public OffenseProjector projector;
	
	@Desc("Effect - what happens when this applies")
	@InlinePolymorphic(AbilEffectMDO.class)
	public PolymorphicLink warhead;

}
