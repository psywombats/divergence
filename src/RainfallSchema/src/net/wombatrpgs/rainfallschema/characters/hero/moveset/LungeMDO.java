/**
 *  LungeMDO.java
 *  Created on Sep 30, 2013 2:40:39 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * A lunging attack (like snakebite)
 */
@Path("characters/hero/moveset/")
public class LungeMDO extends AttackMDO {
	
	@Desc("Instantenous velocity - Actor's velocity is boosted immediately by "
			+ "this amount in the direction of movement, in px/s")
	public Integer instantVelocity;

}
