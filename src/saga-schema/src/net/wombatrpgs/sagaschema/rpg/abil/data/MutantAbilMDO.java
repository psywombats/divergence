/**
 *  MutantAbilMDO.java
 *  Created on May 24, 2014 7:55:21 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * An ability entry: basically an ability and how to get it.
 */
public class MutantAbilMDO extends HeadlessSchema {
	
	@Desc("Ability")
	@SchemaLink(CombatItemMDO.class)
	public String abil;
	
	@Desc("Chance - 0-100, 50 indicates this abil is learned 50% of the time "
			+ "it's selected, 100 is always, 2 or 3 is really rare")
	public Integer chance;
	
	@Desc("Level - probability of learning this abil is halved if fighting "
			+ "monsters below this level")
	public Integer level;

}
