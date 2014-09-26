/**
 *  EventEncounterMDO.java
 *  Created on Sep 23, 2014 9:46:47 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.events;

import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterSetMDO;

/**
 * MDO to generate encounter squares into.
 */
@Path("maps/")
public class EventEncounterMDO extends EventMDO {
	
	@Desc("Encounter set - set of encounters to choose from in this area")
	@SchemaLink(EncounterSetMDO.class)
	public String mdo;

}
