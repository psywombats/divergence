/**
 *  PushSchema.java
 *  Created on Dec 28, 2012 12:16:29 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Push and pull move.
 */
@Path("hero/moveset/")
public class PushMDO extends MoveMDO {
	
	@Desc("Target velocity -- negative goes towards hero, in pixels/second")
	public Integer targetVelocity;
	
	@Desc("Acceleration -- always positive, in pixels/second^2")
	public Integer acceleration;

}
