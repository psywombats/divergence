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
public class WallTilesMDO extends MainSchema {
	
	@Desc("Bottom tile")
	@InlineSchema(TileMDO.class)
	public TileMDO b;
	
	@Desc("Top tile")
	@InlineSchema(TileMDO.class)
	public TileMDO t;
	
	@Desc("Bottom pillar tile")
	@InlineSchema(TileMDO.class)
	public TileMDO bm;
	
	@Desc("Top pillar tile")
	@InlineSchema(TileMDO.class)
	public TileMDO tm;
	
	@Desc("Bottom right tile")
	@InlineSchema(TileMDO.class)
	public TileMDO br;
	
	@Desc("Top right tile")
	@InlineSchema(TileMDO.class)
	public TileMDO tr;
	
	@Desc("Bottom left tile")
	@InlineSchema(TileMDO.class)
	public TileMDO bl;
	
	@Desc("Top left tile")
	@InlineSchema(TileMDO.class)
	public TileMDO tl;

}
