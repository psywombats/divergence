/**
 *  MobilityMDO.java
 *  Created on Jan 26, 2013 6:16:38 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A class to store information on how a character should move, such as its
 * maximum velocities, acceleration information, etc.
 */
@Path("characters/")
public class MobilityMDO extends MainSchema {
	
	@Desc("Movement speed - how fast this character normally moves, in px/s")
	public Integer walkVelocity;
	
	@Desc("Acceleration - how fast this character gets up to speed and stops, in px/s^2")
	public Integer acceleration;
	
	@Desc("Deceleration - how fast this character slows to normal controllable " +
			"speeds when pushed/speeding, in px/s^2")
	public Integer decceleration;

}
