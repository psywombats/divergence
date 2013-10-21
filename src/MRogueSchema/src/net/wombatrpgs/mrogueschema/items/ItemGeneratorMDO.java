/**
 *  ItemList.java
 *  Created on Oct 20, 2013 7:18:32 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * A bunch of items strung together.
 */
@Path("items/")
public class ItemGeneratorMDO extends MainSchema {
	
	@Desc("All the items in this list")
	@SchemaLink(ItemMDO.class)
	public String[] items;
	
	@Desc("How many items to spawn per floor")
	public Integer firstTreasure;

}
