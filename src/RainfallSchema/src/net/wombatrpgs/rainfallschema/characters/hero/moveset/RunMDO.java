/**
 *  RunMDO.java
 *  Created on Apr 11, 2013 5:07:21 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;

/**
 * Affects the user's max speed.
 */
@Path("characters/hero/moveset/")
public class RunMDO extends MoveMDO {
	
	@Desc("New max rate of movement, in px/s")
	public Integer newRate;

}
