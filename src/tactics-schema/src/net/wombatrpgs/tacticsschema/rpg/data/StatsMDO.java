/**
 *  Stats.java
 *  Created on Feb 12, 2014 2:13:42 AM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * All stats a unit can have!
 */
public class StatsMDO extends HeadlessSchema {
	
	@Desc("Move - number of tiles this unit can move per turn, in tiles")
	@DefaultValue("5")
	public Integer move;

}
