/**
 *  MapMDO.java
 *  Created on Nov 12, 2012 6:13:46 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * A map contains not only a TMX but other vital data as well!
 */
@Path("maps/")
public class MapMDO extends MainSchema {
	
	@Desc("The test map to load")
	@FileLink("maps")
	public String map;

}
