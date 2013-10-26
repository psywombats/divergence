/**
 *  GeneratedPotionMDO.java
 *  Created on Oct 21, 2013 10:08:52 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * A spellbook that gets generated on a prefix/suffix basis.
 */
@Path("items/")
public class GeneratedSpellbookMDO extends ItemMDO {
	
	@Desc("All possible prefixes the spell could have")
	@SchemaLink(SpellbookPrefixMDO.class)
	public String[] prefixes;
	
	@Desc("All possible types the spell could be")
	@SchemaLink(SpellbookTypeMDO.class)
	public String[] types;

}
