/**
 *  AbilityMDO.java
 *  Created on Feb 24, 2014 7:30:23 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.tacticsschema.rpg.abil.data.RangeType;
import net.wombatrpgs.tacticsschema.rpg.abil.data.WarheadMDO;

/**
 * MDO for actions.
 */
@Path("rpg/ability")
public class AbilityMDO extends MainSchema {
	
	@Desc("Range")
	public RangeType range;
	
	@Desc("Warhead - corresponds to an in-code definition for an effect")
	@SchemaLink(WarheadMDO.class)
	public String warhead;

}
