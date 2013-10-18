/**
 *  StairTilesMDO.java
 *  Created on Oct 18, 2013 12:10:15 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.maps.data.TileMDO;

/**
 * Tiles for stairs?
 */
@Path("maps/")
public class StairTilesMDO extends MainSchema {
	
	@Desc("The top tile of the stairwell")
	@InlineSchema(TileMDO.class)
	public TileMDO t;
	
	@Desc("The bottom tile of the stairwell")
	@InlineSchema(TileMDO.class)
	public TileMDO b;

}
