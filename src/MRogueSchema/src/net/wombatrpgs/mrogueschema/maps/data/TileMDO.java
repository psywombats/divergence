/**
 *  TileMDO.java
 *  Created on Oct 4, 2013 1:10:17 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * Tile archetype data.
 */
@Path("maps/")
public class TileMDO extends HeadlessSchema {
	
	@Desc("Appearance - image that this tile looks like, a square PNG")
	@FileLink("tiles")
	@Nullable
	public String appearance;

}
