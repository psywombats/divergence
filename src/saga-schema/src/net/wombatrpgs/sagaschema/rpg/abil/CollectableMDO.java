/**
 *  CollectableMDO.java
 *  Created on Sep 22, 2014 10:17:15 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;

/**
 * An object to be hoarded! Probably doesn't need instantiation.
 */
@Path("rpg/")
public class CollectableMDO extends MainSchema {
	
	@Desc("Display name - special symbols are allowed, 8 characters max maybe?")
	public String displayName;

}
