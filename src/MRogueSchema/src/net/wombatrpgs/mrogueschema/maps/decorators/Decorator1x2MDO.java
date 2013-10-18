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
import net.wombatrpgs.mrogueschema.maps.decorators.data.SingleDecoratorMDO;

/**
 * A decorator 1x2
 */
@Path("maps/decorators/")
public class Decorator1x2MDO extends SingleDecoratorMDO {
	
	@Desc("The top tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO t;
	
	@Desc("The bottom tile to replace with")
	@InlineSchema(TileMDO.class)
	public TileMDO b;

}
