/**
 *  ItemMDO.java
 *  Created on Oct 20, 2013 6:43:48 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;

/**
 * All items should extend from this.
 */
@ExcludeFromTree
public class ItemMDO extends MainSchema {
	
	@Desc("Name - displayed in-game")
	public String name;
	
	@Desc("Description - displayed in-game")
	public String gameDesc;
	
	@Desc("Rarity - will be discarded with this %, 0-1")
	@DefaultValue("0")
	public Float rarity;
	
	@Desc("Icon - what this item looks like on the ground")
	@FileLink("items")
	public String icon;
	
}
