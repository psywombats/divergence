/**
 *  MonsterGeneratrorMDO.java
 *  Created on Oct 13, 2013 5:25:21 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.characters.GlobalMonsterListMDO;

/**
 * A thing to spawn monsters on the map.
 */
@Path("maps/")
public class MonsterGeneratorMDO extends MainSchema {
	
	@Desc("Monster list - this is the prefixes/suffixes of all available "
			+ "monsters, but you can filter it later")
	@SchemaLink(GlobalMonsterListMDO.class)
	public String list;
	
	@Desc("Density - maximum number of monsters that can appear per 100*100 "
			+ "chunk of map")
	@DefaultValue("3")
	public Float density;
	
	@Desc("Respawn rate - average number of turns it takes for a monster to "
			+ "be regenerated after death")
	public Integer respawnRate;
	
	@Desc("Loot rate - chance that monsters carry loot, 0-1")
	public Float loot;

}
