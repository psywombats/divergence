/**
 *  ClassMDO.java
 *  Created on Oct 29, 2013 1:35:15 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;
import net.wombatrpgs.mrogueschema.graphics.DirMDO;

/**
 * Initial stat modifiers and things.
 */
@Path("characters/")
public class ClassMDO extends MainSchema {
	
	@Desc("Name - displayed in-game")
	public String className;
	
	@Desc("Description - displayed in-game")
	public String classDesc;
	
	@Desc("Animation - will change the player to look like this")
	@SchemaLink(DirMDO.class)
	@Nullable
	public String appearance;
	
	@Desc("Stats - will be treated as a modifier to hero base")
	@InlineSchema(StatsMDO.class)
	public StatsMDO stats;
	
	@Desc("Abilities - in addition to whatever defaults the hero has")
	@SchemaLink(AbilityMDO.class)
	public String[] abilities;

}
