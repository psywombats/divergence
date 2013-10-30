/**
 *  EnemyModMDO.java
 *  Created on Oct 29, 2013 5:33:37 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;
import net.wombatrpgs.mrogueschema.graphics.data.ColorMDO;

/**
 * MOD != MDO. This is a prefix for a monster, sort of.
 */
@Path("characters/")
public class EnemyModMDO extends MainSchema {
	
	@Desc("DL - danger level that gets added to the monster's level")
	public Integer danger;
	
	@Desc("Flash color - almost always use this is the monster gets more powerful!")
	@InlineSchema(ColorMDO.class)
	public ColorMDO color;
	
	@Desc("Abilities - added to any existing")
	@SchemaLink(AbilityMDO.class)
	public String[] abilities;
	
	@Desc("Stats - treated as a modifier")
	@InlineSchema(StatsMDO.class)
	public StatsMDO stats;

}
