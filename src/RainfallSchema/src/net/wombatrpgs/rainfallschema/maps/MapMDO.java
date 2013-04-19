/**
 *  MapMDO.java
 *  Created on Nov 12, 2012 6:13:46 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps;

import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectMDO;

/**
 * A map contains not only a TMX but other vital data as well!
 */
@Path("maps/")
public class MapMDO extends MainSchema {
	
	@Desc("Map file - Tield .tmx map, located in maps folder")
	@FileLink("maps")
	public String map;
	
	@Desc("Effect - graphical effect that plays on map, or none")
	@SchemaLink(EffectMDO.class)
	@Nullable
	public String effect;

}
