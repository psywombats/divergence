/**
 *  AbilTeleportMDO.java
 *  Created on Oct 27, 2013 5:51:15 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;

/**
 * Where am I now? Huh?
 */
@Path("characters/effects/")
public class AbilTeleportMDO extends AbilityEffectMDO {
	
	@Desc("Minimum possible displacement for the teleport, in tiles")
	public Float scatterMin;
	
	@Desc("Maximum possible displacement for the teleport, in tiles")
	public Float scatterMax;

}
