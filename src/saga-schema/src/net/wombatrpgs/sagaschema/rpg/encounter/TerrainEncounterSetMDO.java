/**
 *  TerrainEncounterSetMDO.java
 *  Created on Sep 10, 2014 10:36:32 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.encounter;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.encounter.data.TerrainEncounterSetMemberMDO;

/**
 * Collection of encounter sets to use based on terrain.
 */
@Path("rpg/")
public class TerrainEncounterSetMDO extends MainSchema {
	
	@Desc("Terrain encounter tables")
	@InlineSchema(TerrainEncounterSetMemberMDO.class)
	public TerrainEncounterSetMemberMDO[] members;

}
