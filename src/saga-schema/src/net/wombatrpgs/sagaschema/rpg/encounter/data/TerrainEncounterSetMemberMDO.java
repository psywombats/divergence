/**
 *  TerrainEncounterSetMemberMDO.java
 *  Created on Sep 10, 2014 10:37:03 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.encounter.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterSetMDO;

/**
 * A terrain and encounter set pair
 */
public class TerrainEncounterSetMemberMDO extends HeadlessSchema {
	
	@Desc("Terrain - in format [tilesetname]/[id], find both properties in the Tiled editor")
	public String terrain;
	
	@SchemaLink(EncounterSetMDO.class)
	public String encounters;

}
