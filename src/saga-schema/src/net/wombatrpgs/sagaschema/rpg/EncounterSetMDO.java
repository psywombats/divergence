/**
 *  EncounterAreaMDO.java
 *  Created on Jun 27, 2014 7:41:11 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * A set of possible encounters in a rectangle.
 */
@Path("rpg/")
public class EncounterSetMDO extends MainSchema {
	
	@Desc("Possible encounters in this set")
	@SchemaLink(EncounterMDO.class)
	public String[] encounters;
	
	@Desc("Frequency - encounters will occur once per this many steps")
	public Integer steps;

}
