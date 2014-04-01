/**
 *  AbilMultihit.java
 *  Created on Apr 1, 2014 2:23:53 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data.warheads;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseProjector;
import net.wombatrpgs.sagaschema.rpg.data.Stat;

/**
 * Multi-hit attack (2pincer, 3head, etc)
 */
public class AbilMultihitMDO extends AbilEffectMDO {
	
	@Desc("Projector - what does this attack hit?")
	public OffenseProjector projector;
	
	@Desc("Base power - power of each attack, before modifiers")
	public Integer power;
	
	@Desc("First hits - how many times this attack hits on first use")
	public Integer hitsFirst;
	
	@Desc("Successive hits - how many times this attack hits on other use")
	public Integer hitsLast;
	
	@Desc("Attack stat - this stat is doubled and added to base power")
	@Nullable
	public Stat attackStat;
	
	@Desc("Defend stat - this stat is subtracted from incoming damage, for "
			+ "status this stat becomes % to block")
	@Nullable
	public Stat defendStat;

}
