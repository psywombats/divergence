/**
 *  EncounterSetMemberMDO.java
 *  Created on Sep 8, 2014 9:08:26 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.encounter.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterMDO;

/**
 * A member of an encounter set. Has an encounter and a weight.
 */
public class EncounterSetMemberMDO extends HeadlessSchema {
	
	@Desc("The encounter in this set")
	@SchemaLink(EncounterMDO.class)
	public String encounter;
	
	@Desc("The weight this encounter will appear vs others in the set")
	public Integer weight;

}
