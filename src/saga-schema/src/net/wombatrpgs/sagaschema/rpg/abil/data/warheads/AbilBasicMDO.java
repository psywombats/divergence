/**
 *  AbilBasic.java
 *  Created on Mar 31, 2014 9:16:48 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.DamageType;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseSideEffect;
import net.wombatrpgs.sagaschema.rpg.data.Stat;

/**
 * Inflicts something on whoever, both damage and status.
 */
public class AbilBasicMDO extends AbilEffectMDO {
	
	@Desc("Projector - what does this attack hit?")
	public OffenseProjector projector;
	
	@Desc("Damage types - all of these will be inflicted")
	public DamageType[] damType;
	
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
	
	@Desc("Other flags - varying modifiers on this attack")
	public OffenseSideEffect[] sideEffects;

}
