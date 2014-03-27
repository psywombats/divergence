/**
 *  LoadedMapMDO.java
 *  Created on Jan 4, 2014 9:53:51 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.maps;

import net.wombatrpgs.mgneschema.maps.data.MapMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Map that is stored as a TMX. Will fill this in later, should be for most of
 * the maps.
 */
@Path("maps/")
public class LoadedMapMDO extends MapMDO {
	
	private static final long serialVersionUID = 1L;
	@Desc(".tmx file to load from")
	@FileLink("maps")
	public String file;

}
