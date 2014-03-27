/**
 *  StairTilesMDO.java
 *  Created on Oct 18, 2013 12:10:15 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.maps.gen;

import net.wombatrpgs.mgneschema.maps.data.TileMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Tiles for stairs?
 */
@Path("maps/")
public class StairTilesMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;

	@Desc("The top tile of the stairwell")
	@InlineSchema(TileMDO.class)
	public TileMDO t;
	
	@Desc("The bottom tile of the stairwell")
	@InlineSchema(TileMDO.class)
	public TileMDO b;

}
