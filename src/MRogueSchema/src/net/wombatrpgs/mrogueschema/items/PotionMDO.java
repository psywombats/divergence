/**
 *  PotionMDO.java
 *  Created on Oct 21, 2013 4:20:40 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * Potions potions eat it up yum. This is actually just any consumable.
 */
@Path("items/")
public class PotionMDO extends ItemMDO {
	
	@Desc("Duration - how long, in turns, this potion's effect will last, 0 for forever")
	@DefaultValue("0")
	public Integer duration;

	@Desc("Stats - the stats modifier that will be applied")
	@InlineSchema(StatsMDO.class)
	public StatsMDO mod;
}