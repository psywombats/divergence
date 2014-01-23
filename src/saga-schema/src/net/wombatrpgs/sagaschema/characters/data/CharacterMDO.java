/**
 *  EventMDO.java
 *  Created on Nov 12, 2012 4:45:16 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.characters.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.graphics.DirMDO;

/**
 * An interactive component on the map is called an "Event." (it's an entity,
 * but let's pretend, okay?)
 */
@ExcludeFromTree
public class CharacterMDO extends MainSchema {
	
	@Desc("Animation - what this event looks like")
	@SchemaLink(DirMDO.class)
	@Nullable
	public String appearance;

}
