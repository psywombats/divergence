/**
 *  ItemSpellbookMDO.java
 *  Created on Oct 20, 2013 6:51:54 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * PK FIYAH mkII
 */
@Path("items/")
public class SpellbookMDO extends ItemMDO {
	
	@Desc("Ability - what this spellbook teaches")
	@SchemaLink(AbilityMDO.class)
	public String ability;

}
