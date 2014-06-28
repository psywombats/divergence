/**
 *  EncounterMDO.java
 *  Created on Jun 27, 2014 7:23:54 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.data.EncounterMemberMDO;

/**
 * A potential encounter in an encounter group.
 */
@Path("rpg/")
public class EncounterMDO extends MainSchema {
	
	@Desc("Potential members")
	@InlineSchema(EncounterMemberMDO.class)
	public EncounterMemberMDO[] members;
	
	@Desc("Weight - compared against other encounters in the set for the "
			+ "likelihood to see this encounter vs others")
	public Integer weight;

}
