/**
 *  CharacterSetMDO.java
 *  Created on Oct 22, 2013 3:04:05 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.cutscene.data.ArchetypeMDO;

/**
 * An archetype and a few names.
 */
@Path("cutscene/")
public class CharacterSetMDO extends MainSchema {
	
	@Desc("All the archetypes")
	@InlineSchema(ArchetypeMDO.class)
	public ArchetypeMDO[] typeName;

}
