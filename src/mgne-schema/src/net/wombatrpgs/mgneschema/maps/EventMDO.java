/**
 *  EventMDO.java
 *  Created on Jan 28, 2014 2:48:50 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.maps;

import net.wombatrpgs.mgneschema.graphics.DirMDO;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * MDO for everything constructed from a Tiled map, or just everything in a map
 * grid in general.
 */
@Path("events/")
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
	
	@Desc("onAdd - lua value, script is called when event is added to map")
	@DefaultValue("")
	public String onAdd;
	
	@Desc("onRemove - lua value, script is called when event is removed from map")
	@DefaultValue("")
	public String onRemove;
	
	@Desc("onTurn - lua value, script is called every time it's this event's turn")
	@DefaultValue("")
	public String onTurn;
	
	@Desc("onCollide - lua value, script is called when player walks into this event")
	@DefaultValue("")
	public String onCollide;
	
	@Desc("onInteract - lua value, script is called when player talks to this event")
	@DefaultValue("")
	public String onInteract;

}
