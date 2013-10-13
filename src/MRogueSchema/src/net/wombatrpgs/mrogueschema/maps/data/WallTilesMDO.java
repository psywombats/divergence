/**
 *  WallTilesMDO.java
 *  Created on Oct 12, 2013 4:55:34 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mrogueschema.maps.TileMDO;

/**
 * Wall tiles that make up a wall.
 */
public class WallTilesMDO extends HeadlessSchema {
	
	@Desc("Upper tile")
	@InlineSchema(TileMDO.class)
	public TileMDO upper;
	
	@Desc("Lower tile")
	@InlineSchema(TileMDO.class)
	public TileMDO lower;

}
