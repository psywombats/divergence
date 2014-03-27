/**
 *  Decorator1x1.java
 *  Created on Oct 13, 2013 6:13:29 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.maps.decorators;

import net.wombatrpgs.mgneschema.maps.data.TileMDO;
import net.wombatrpgs.mgneschema.maps.data.TileType;
import net.wombatrpgs.mgneschema.maps.decorators.data.SingleDecoratorMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Whose idea was this?
 */
@Path("maps/decorators/")
public class Decorator1x2SpecialMDO extends SingleDecoratorMDO {
	
	private static final long serialVersionUID = 1L;

	@Desc("The top tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO t;
	
	@Desc("The tile to replace top with (bottom uses standard)")
	public TileType originalT;
	
	@Desc("The bottom tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO b;

}
