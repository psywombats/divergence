/**
 *  EventDoorMDO.java
 *  Created on Sep 17, 2014 11:54:56 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.events;

import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * A locked door that requires a key.
 */
@Path("maps/")
public class EventDoorMDO extends EventMDO {
	
	@Desc("Key - the item used to unlock the door")
	@SchemaLink(CombatItemMDO.class)
	public String keyItem;

}
