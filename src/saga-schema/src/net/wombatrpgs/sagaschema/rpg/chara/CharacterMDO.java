/**
 *  CharacterMDO.java
 *  Created on Apr 2, 2014 9:41:38 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.stats.StatSetMDO;

/**
 * A playable character, or at least a blank, recruitable one.
 */
@Path("rpg/")
public class CharacterMDO extends MainSchema {
	
	@Desc("Name - default in the case of players, or else enemy name")
	public String name;
	
	@Desc("Race")
	public Race race;
	
	@Desc("Equipped items/abilities")
	@SchemaLink(CombatItemMDO.class)
	public String equipped[];
	
	@InlineSchema(StatSetMDO.class)
	public StatSetMDO stats;

}
