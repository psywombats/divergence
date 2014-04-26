/**
 *  AbilCounterMDO.java
 *  Created on Apr 1, 2014 3:17:01 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.PolymorphicLink;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlinePolymorphic;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.DefenseFlag;
import net.wombatrpgs.sagaschema.rpg.stats.StatModMDO;

/**
 * Counterattacks.
 */
public class EffectDefendMDO extends EffectAllyTargetMDO {
	
	@Desc("Shield value - raises evasion rate by this amount, usually 0-100")
	@DefaultValue("0")
	public Integer shielding;
	
	@Desc("Trigger types - when any of these are taken, counter will trigger")
	public DamageType[] triggerTypes;
	
	@Desc("Counter - an offensive ability to launch when triggered")
	@InlinePolymorphic(AbilEffectMDO.class)
	@Nullable
	public PolymorphicLink warhead;
	
	@Desc("Stat modifiers - applied to the defended characters for the turn")
	@InlineSchema(StatModMDO.class)
	public StatModMDO stats;
	
	@Desc("Other flags")
	public DefenseFlag[] effects;
	
}
