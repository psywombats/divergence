/**
 *  PartyMDO.java
 *  Created on Feb 13, 2014 2:33:01 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A listing of a bunch of units.
 */
@Path("rpg/")
public class PartyMDO extends MainSchema {
	
	@Desc("All units in the party")
	@SchemaLink(PlayerUnitMDO.class)
	public String[] units;

}
