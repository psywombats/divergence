/**
 *  EventMDO.java
 *  Created on Mar 2, 2013 6:01:25 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.maps.data.RenderSortType;

/**
 * Custom objects with IDs, for hardcoded stuff.
 */
@Path("maps/")
public class CustomEventMDO extends CharacterEventMDO {
	
	@Desc("ID - Tiled objects that are this event will use this for their id " +
			"property and the engine will use this to special-case the " +
			"object.")
	public String id;
	
	@Desc("Relative hero display")
	@DefaultValue("SAME_LEVEL_AS_HERO")
	public RenderSortType sort;

}
