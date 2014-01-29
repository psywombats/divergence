/**
 *  EventMDO.java
 *  Created on Jan 28, 2014 2:48:50 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.maps;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.graphics.DirMDO;
import net.wombatrpgs.sagaschema.maps.data.MoveType;

/**
 * MDO for everything constructed from a Tiled map, or just everything in a map
 * grid in general.
 */
public class EventMDO extends MainSchema {
	
	@Desc("Animation - what this event looks like, or null for invisible")
	@SchemaLink(DirMDO.class)
	@DefaultValue("")
	@Nullable
	public String appearance;
	
	@Desc("Name - identifier for the NPC, it's fine to be blank")
	@DefaultValue("")
	public String name;
	
	@Desc("Group - any groups this NPC is in, when in doubt just leave blank, space seperated")
	@DefaultValue("")
	public String groups;
	
	@Desc("Move type - should this event wander around?")
	@DefaultValue("STAY_STILL")
	public MoveType move;

}
