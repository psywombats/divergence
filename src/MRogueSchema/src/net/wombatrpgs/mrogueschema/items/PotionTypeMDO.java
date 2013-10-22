/**
 *  PotionTypeMDO.java
 *  Created on Oct 22, 2013 4:33:38 AM for project MRogueSchema
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
 * Suffix for potion.
 */
@Path("items/")
public class PotionTypeMDO extends MainSchema {
	
	@Desc("Name - in game, for instance 'potion', 'boost potion'")
	public String typename;
	
	@Desc("Description - in game, for instance 'lasts 10 turns'")
	public String typedesc;
	
	@Desc("Duration - how long this potion lasts, or 0 for forever")
	public Integer duration;
	
	@Desc("Effectivity - what percentage of the permanent dose should be applied, 0 to 1")
	public Float rate;
	
	@Desc("Extra duration - duration for the extra stats")
	public Integer extraDuration;
	
	@Desc("Extra stats - applied in addition to the stats from potion prefix")
	@InlineSchema(StatsMDO.class)
	public StatsMDO extraStats;

}
