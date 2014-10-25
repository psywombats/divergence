/**
 *  MapMDO.java
 *  Created on Jan 3, 2014 7:58:49 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.maps.data;

import net.wombatrpgs.mgneschema.graphics.effects.data.EffectMDO;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * Superclass for generated and Tiled maps.
 */
@ExcludeFromTree
public class MapMDO extends MainSchema {
	
	@Desc("Effect - graphical effect that plays on map, or none")
	@SchemaLink(EffectMDO.class)
	@Nullable
	public String effect;

}
