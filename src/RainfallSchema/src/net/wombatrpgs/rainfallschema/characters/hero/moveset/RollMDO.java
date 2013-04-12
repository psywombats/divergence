/**
 *  RollMDO.java
 *  Created on Apr 11, 2013 9:57:04 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;

/**
 * DO A BARREL ROLL
 */
@Path("characters/hero/moveset/")
public class RollMDO extends MoveMDO {
	
	@Desc("Length - How fall the roll carries, in px. The duration/velocity is taken from the animation.")
	public Integer length;

}
