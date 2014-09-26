/**
 *  EventChestMDO.java
 *  Created on Sep 23, 2014 8:07:02 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.events;

import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.events.data.KeyItemType;
import net.wombatrpgs.sagaschema.rpg.abil.CollectableMDO;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * SaGa generic treasure chest event.
 */
@Path("maps/")
public class EventChestMDO extends EventMDO {
	
	@Desc("Item - the item in the chest, or empty")
	@SchemaLink(CombatItemMDO.class)
	@Nullable
	public String item;
	
	@Desc("Collectable - the collectable in the chest, or empty")
	@SchemaLink(CollectableMDO.class)
	public String collectable;
	
	@Desc("Key item - for keys and plot items that can be found in chests")
	@DefaultValue("GENERIC")
	public KeyItemType keyItem;

}
