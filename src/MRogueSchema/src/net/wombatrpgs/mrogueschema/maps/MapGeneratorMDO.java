/**
 *  MapGenerator.java
 *  Created on Oct 4, 2013 1:30:51 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.maps.data.GenerationType;
import net.wombatrpgs.mrogueschema.maps.data.WallTilesMDO;

/**
 * Something to procedurally generate maps.
 */
@Path("maps/")
public class MapGeneratorMDO extends MainSchema {
	
	@Desc("Generation type - parameters etc to the procedural algorithm that generates this area")
	public GenerationType generator;
	
	@Desc("Floor tile(s)")
	@InlineSchema(TileMDO.class)
	public TileMDO[] floorTiles;
	
	@Desc("Wall tile(s)")
	@InlineSchema(WallTilesMDO.class)
	public WallTilesMDO[] wallTiles;
	
	@Desc("Ceiling tile(s)")
	@InlineSchema(TileMDO.class)
	public TileMDO[] ceilingTiles;

}
