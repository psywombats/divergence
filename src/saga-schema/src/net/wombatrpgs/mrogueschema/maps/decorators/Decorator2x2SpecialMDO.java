/**
 *  Decorator1x1.java
 *  Created on Oct 13, 2013 6:13:29 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.decorators;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.maps.data.TileMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;
import net.wombatrpgs.mrogueschema.maps.decorators.data.ShelfMode;
import net.wombatrpgs.mrogueschema.maps.decorators.data.SingleDecoratorMDO;

/**
 * A decorator 1x2
 */
@Path("maps/decorators/")
public class Decorator2x2SpecialMDO extends SingleDecoratorMDO {
	
	@Desc("The top left tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO tl;
	
	@Desc("The top left tile type to replace")
	public TileType tlOriginal;
	
	@Desc("The top right to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO tr;
	
	@Desc("The top left right type to replace")
	public TileType trOriginal;
	
	@Desc("The bottom left tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO bl;
	
	@Desc("The bottom left tile type to replace")
	public TileType blOriginal;
	
	@Desc("The bottom right tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO br;
	
	@Desc("The bottom right tile type to replace")
	public TileType brOriginal;
	
	@Desc("How to handle shelves")
	public ShelfMode shelfMode;

}
