/**
 *  AbilCounterMDO.java
 *  Created on Apr 1, 2014 3:17:01 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AllyProjector;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.DefenseFlag;
import net.wombatrpgs.sagaschema.rpg.chara.data.Status;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Counterattacks.
 */
public class EffectDefendMDO extends AbilEffectMDO {
	
	@Desc("Projector - what this ability covers")
	@DefaultValue("USER")
	public AllyProjector projector;
	
	@Desc("Trigger types - when any of these are taken, will trigger")
	public DamageType[] triggerTypes;
	
	@Desc("Shield value - raises evasion rate by this amount, usually 0-100")
	@DefaultValue("0")
	public Integer shielding;
	
	@Desc("Status - inflicted on trigger to the attacker")
	@DefaultValue("None")
	@Nullable
	public Status status;

	@Desc("Counter type - damage inflicted when triggered")
	@DefaultValue("None")
	@Nullable
	public DamageType counterType;
	
	@Desc("Base power - multiplier of the retaliation damage, or base inflict "
			+ "chance for status conditions")
	@DefaultValue("0")
	public Integer power;
	
	@Desc("Attack stat - this stat is quartered and multiplied by base power, "
			+ "or goes towards status inflict accuracy")
	@DefaultValue("STR")
	@Nullable
	public Stat attackStat;
	
	@Desc("Defend stat - this stat is subtracted from incoming damage, or "
			+ "subtracted from status inflict accuracy")
	@DefaultValue("DEF")
	@Nullable
	public Stat defendStat;
	
	@Desc("Other flags")
	public DefenseFlag[] effects;
	
}
