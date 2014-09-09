/**
 *  EncounterMDO.java
 *  Created on Sep 8, 2014 9:13:09 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.encounter;

import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.sagaschema.rpg.encounter.data.EncounterMemberMDO;

/**
 * A group of encounter members.
 */
@Path("rpg/")
public class EncounterMDO extends MainSchema {
	
	@Desc("Members in this encounter")
	@InlineSchema(EncounterMemberMDO.class)
	public EncounterMemberMDO[] members;

}
