/**
 *  PotionPrefixMDO.java
 *  Created on Oct 21, 2013 10:17:10 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;

/**
 * Determines what stats are affected by a given potion.
 */
@Path("items/")
public class PotionPrefixMDO extends MainSchema {
	
	@Desc("Name - in-game, for instance 'strength,' 'wizard's,' 'anti-magical' etc")
	public String prename;
	
	@Desc("Description in-game, for instance 'increase melee damage'")
	public String predesc;
	
	@Desc("Stats - base stats for a permanent dose")
	@InlineSchema(StatsMDO.class)
	public StatsMDO stats;

}
