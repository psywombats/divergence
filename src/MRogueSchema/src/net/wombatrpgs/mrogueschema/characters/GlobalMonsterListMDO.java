/**
 *  MonsterGeneratorMDO.java
 *  Created on Oct 12, 2013 3:37:23 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mrogueschema.characters.data.EnemyModEntryMDO;
import net.wombatrpgs.mrogueschema.characters.data.MonsterNameMDO;

/**
 * A generator for monsters from a prefix/suffix format.
 */
@Path("characters/")
public class GlobalMonsterListMDO extends MainSchema {
	
	@Desc("Types")
	@InlineSchema(MonsterNameMDO.class)
	public MonsterNameMDO[] names;
	
	@Desc("Prefixes")
	@InlineSchema(EnemyModEntryMDO.class)
	public EnemyModEntryMDO[] prefixes;

}
