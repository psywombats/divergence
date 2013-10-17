/**
 *  DecoratorSetMDO.java
 *  Created on Oct 17, 2013 2:42:20 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.decorators;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.maps.decorators.data.DecoratorMDO;

/**
 * A bunch of decorators bunched together. This can be things like all
 * paintings, all candles, all level 1 deocrations, etc.
 */
@Path("maps/decorators/")
public class DecoratorSetMDO extends DecoratorMDO {
	
	@Desc("Decorators - all children that should be considered for decoration")
	@SchemaLink(DecoratorMDO.class)
	public String[] decorators;

}
