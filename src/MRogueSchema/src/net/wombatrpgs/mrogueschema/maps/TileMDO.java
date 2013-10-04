/**
 *  TileMDO.java
 *  Created on Oct 4, 2013 1:10:17 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.maps.data.PassabilityType;

/**
 * Tile archetype data.
 */
@Path("maps/")
public class TileMDO extends MainSchema {
	
	@Desc("Passability type - is this tile passable by characters?")
	@DefaultValue("PASSABLE")
	public PassabilityType passable;
	
	@Desc("Appearance - image that this tile looks like, a square PNG")
	@FileLink("tiles")
	@Nullable
	public String appearance;

}
