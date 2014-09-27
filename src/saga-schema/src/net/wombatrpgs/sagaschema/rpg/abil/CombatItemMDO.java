/**
 *  AbilityMDO.java
 *  Created on Feb 24, 2014 7:30:23 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.InlinePolymorphic;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.PolymorphicLink;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleAnimMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilityType;
import net.wombatrpgs.sagaschema.rpg.abil.data.EquipmentFlag;
import net.wombatrpgs.sagaschema.rpg.stats.NumericStatModMDO;

/**
 * MDO for actions.
 */
@Path("rpg/")
public class CombatItemMDO extends MainSchema {
	
	@Desc("Ability name - displayed in-game, potentially with $A special char codes")
	public String abilityName;
	
	@Desc("Type")
	public AbilityType type;
	
	@Desc("Uses - or zero for infinite uses")
	@DefaultValue("0")
	public Integer uses;
	
	@Desc("Cost - or zero for unsellable, halved for resale rate")
	@DefaultValue("0")
	public Integer cost;
	
	@Desc("Animation - graphical effect that plays when this item is used in battle")
	@SchemaLink(BattleAnimMDO.class)
	@Nullable
	public String anim;
	
	@Desc("Equipment types - characters can't equip two items that share a flag")
	public EquipmentFlag[] equip;
	
	@Desc("Effect - what happens when this applies")
	@InlinePolymorphic(AbilEffectMDO.class)
	@Nullable
	public PolymorphicLink warhead;
	
	@Desc("Tier - mostly cosmetic, but can be used to autogenerate robostats")
	public Integer tier;
	
	@Desc("Robo stats - these boosts are granted to robots that equip this")
	@InlineSchema(NumericStatModMDO.class)
	public NumericStatModMDO robostats;

}
