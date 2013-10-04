/**
 *  MapMDO.java
 *  Created on Nov 12, 2012 6:13:46 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps;

import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mrogueschema.graphics.effects.data.EffectMDO;

/**
 * A map contains not only a TMX but other vital data as well!
 * MR: This now just contains info on how to generate the map, not its file.
 */
@Path("maps/")

public class MapMDO extends MainSchema {
	
	@Desc("Map width, in tiles")
	public Integer mapWidth;
	
	@Desc("Map height, in tiles")
	public Integer mapHeight;
	
	@Desc("Generator - The algorithm that will be used to generate this map")
	@SchemaLink(MapGeneratorMDO.class)
	public String generator;
	
	@Desc("Effect - graphical effect that plays on map, or none")
	@SchemaLink(EffectMDO.class)
	@Nullable
	public String effect;

}
