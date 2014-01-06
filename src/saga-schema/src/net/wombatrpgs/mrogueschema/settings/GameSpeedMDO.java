/**
 *  GameSpeedMDO.java
 *  Created on Nov 25, 2012 12:01:22 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Static information about game render and movement speed.
 */
@Path("settings/")
public class GameSpeedMDO extends MainSchema {
	
	@Desc("Target render rate, in frames per second. At rates lower than " +
			"this, the game will slow down.")
	public Integer framerate;
	
	@Desc("Base delay, in seconds. Every time the player makes a move, this "
			+ "much time elapses before they show up at their next square.")
	public Float delay;

}
