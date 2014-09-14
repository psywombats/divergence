/**
 *  ShopMDO.java
 *  Created on Sep 14, 2014 1:48:15 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * It's easier to fill out a shop MDO than list a hundred items.
 */
@Path("rpg/")
public class ShopMDO extends MainSchema {
	
	@Desc("Available items")
	@SchemaLink(CombatItemMDO.class)
	public String[] items;

}
