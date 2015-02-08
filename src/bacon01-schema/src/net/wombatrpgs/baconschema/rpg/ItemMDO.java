/**
 *  ItemMDO.java
 *  Created on Jan 26, 2015 9:40:48 PM for project bacon01-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.baconschema.rpg;

import net.wombatrpgs.baconschema.rpg.data.ItemType;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Inventory item.
 */
@Path("rpg/")
public class ItemMDO extends MainSchema {
	
	@Desc("Item name")
	public String name;
	
	@Desc("Sprite - how it appears on the floor")
	@FileLink("sprites")
	public String sprite;
	
	@Desc("Inventory Icon - how it appears on the floor and in inventory")
	@FileLink("ui")
	public String icon;
	
	@Desc("In-game description")
	public String gameDesc;
	
	@Desc("Item type")
	public ItemType itemType;
	
	@Desc("Notes set")
	@SchemaLink(NotesSetMDO.class)
	public String set;

}
