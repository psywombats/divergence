/**
 *  MonsterGroupMDO.java
 *  Created on May 2, 2014 10:40:39 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A group of monster families.
 */
@Path("rpg/")
public class MeatGroupMDO extends MainSchema {
	
	@SchemaLink(MonsterFamilyMDO.class)
	public String[] families;

}
