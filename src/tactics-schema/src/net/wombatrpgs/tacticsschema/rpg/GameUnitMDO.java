/**
 *  GameUnitMDO.java
 *  Created on Feb 12, 2014 2:16:00 AM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg;

import net.wombatrpgs.mgneschema.graphics.FourDirMDO;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.tacticsschema.rpg.data.StatsMDO;

/**
 * A unit in the RPG has one of these.
 */
@Path("rpg/")
public class GameUnitMDO extends MainSchema {
	
	@Desc("Name - in-game name displayed to player")
	@DefaultValue("")
	public String name;
	
	@Desc("Appearance - used for the appearance on map")
	@SchemaLink(FourDirMDO.class)
	public String appearance;
	
	@Desc("Stats - default")
	@InlineSchema(StatsMDO.class)
	public StatsMDO stats;

}
