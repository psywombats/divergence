/**
 *  RollSchema.java
 *  Created on Dec 14, 2012 12:10:07 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * SOMERSAULTTTTT (is a funny word)
 */
@Path("hero/moves/")
public class RollSchema extends MoveSchema {
	
	@Desc("Duration - how long the roll lasts, in seconds")
	public Float duration;
	
	@Desc("Distance - how far the roll goes, in pixels")
	public Integer distance;

}
