/**
 *  MapLoadTestMDO.java
 *  Created on Nov 8, 2012 9:56:48 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.test;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.maps.MapMDO;

/**
 * Load up and test a map.
 */
@Path("test/")
public class MapLoadTestMDO extends MainSchema {
	
	@Desc("The test map to load")
	@SchemaLink(MapMDO.class)
	public String map;

}
