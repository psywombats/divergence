/**
 *  DashSchema.java
 *  Created on Dec 14, 2012 11:48:09 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Dashing specifications - for all moves where a hero's speed changes.
 */
@Path("hero/moves/")
public class DashSchema extends MoveSchema {
	
	@Desc("Target speed - in pixels/second, could be lower/higher than default")
	public Integer targetMult;
	
	@Desc("Acceleration - in pixels/second^2, value ~100 is medium acceleration")
	public Float acceleration;

}
