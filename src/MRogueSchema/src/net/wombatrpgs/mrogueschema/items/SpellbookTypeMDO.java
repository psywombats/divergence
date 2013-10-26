/**
 *  SpellbookTypeMDO.java
 *  Created on Oct 26, 2013 3:01:14 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * Suffix for spellbook.
 */
@Path("items/")
public class SpellbookTypeMDO extends MainSchema {
	
	@Desc("Name - part of the in-game name, ie 'ball,' 'touch'")
	public String typeName;
	
	@Desc("Description - half of in-game description")
	public String typeDesc;
	
	@Desc("Target type")
	public AbilityTargetType target;
	
	@Desc("Range")
	public Float range;
	
	@Desc("Cost mult - is this many times more expensive than base")
	public Float costMult;
	
	@Desc("Icon - what this item looks like on the ground")
	@FileLink("items")
	public String icon;

}
