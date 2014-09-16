/**
 *  BattleAnimMDO.java
 *  Created on May 23, 2014 8:31:44 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics.banim.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;

/**
 * Parent class for graphical effects.
 */
@ExcludeFromTree
public class BattleAnimMDO extends MainSchema {
	
	@Desc("Sound effect - plays with the animation and on the map when used")
	@FileLink("audio")
	@Nullable
	public String sound;

}
