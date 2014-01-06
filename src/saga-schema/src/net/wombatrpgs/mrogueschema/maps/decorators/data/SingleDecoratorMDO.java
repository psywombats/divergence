/**
 *  DecoratorMDO.java
 *  Created on Oct 13, 2013 6:00:04 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.decorators.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mrogueschema.maps.data.TileType;

/**
 * Head of all decorators!
 */
@ExcludeFromTree
public class SingleDecoratorMDO extends DecoratorMDO {
	
	@Desc("Probabililty this decoration will trigger, 0 is never 1 is always")
	public Float chance;
	
	@Desc("The tile type to replace")
	public TileType original;
	
	@Desc("The new tile type to replace it with")
	public TileType replacement;

}
