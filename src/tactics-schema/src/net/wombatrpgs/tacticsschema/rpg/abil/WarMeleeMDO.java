/**
 *  WarheadMeleeMDO.java
 *  Created on Feb 24, 2014 10:15:39 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.tacticsschema.rpg.abil.data.WarheadMDO;

/**
 * Punch a chump.
 */
@Path("rpg/ability/")
public class WarMeleeMDO extends WarheadMDO {
	
	private static final long serialVersionUID = 1L;
	@Desc("Power - base damage of this attack")
	public Integer power;

}
