/**
 *  AbilCounterMDO.java
 *  Created on Apr 1, 2014 3:17:01 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.DefenseSideEffect;
import net.wombatrpgs.sagaschema.rpg.data.Stat;

/**
 * Counterattacks.
 */
public class AbilCounterMDO extends AbilEffectMDO {
	
	@Desc("Trigger types - when any of these are taken, will trigger")
	public DamageType[] triggerTypes;

	@Desc("Counter types - all of these will be inflicted")
	public DamageType[] counterTypes;
	
	@Desc("Base power - power of the attack before modifiers, for status this "
			+ "power goes towards overcoming status block")
	public Integer power;
	
	@Desc("Attack stat - this stat is doubled and added to base power")
	@Nullable
	public Stat attackStat;
	
	@Desc("Defend stat - this stat is subtracted from incoming damage, for "
			+ "status this stat becomes % to block")
	@Nullable
	public Stat defendStat;
	
	@Desc("Misc effects")
	public DefenseSideEffect[] effects;
	
}
