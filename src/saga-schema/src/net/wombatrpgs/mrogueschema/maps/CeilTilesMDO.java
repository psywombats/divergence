/**
 *  WallTilesMDO.java
 *  Created on Oct 12, 2013 4:55:34 PM for project MRogueSchema
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
 * Wall tiles that make up a wall.
 */
@Path("maps/")
public class CeilTilesMDO extends MainSchema {
	
	@Desc("Top tile - displays everywhere but north of a wall")
	@InlineSchema(TileMDO.class)
	public TileMDO t;
	
	@Desc("Middle tile - displays two tiles north of a wall")
	@InlineSchema(TileMDO.class)
	public TileMDO m;
	
	@Desc("Bottom tile - displays directly north of a wall")
	@InlineSchema(TileMDO.class)
	public TileMDO b;
	
}
