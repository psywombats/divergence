/**
 *  DashSchema.java
 *  Created on Dec 12, 2012 4:39:45 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Base class for all schema that form part of the moveset. Hopefully
 * inheritance works in this little setup. Shouldn't appear on the tree either,
 * assuming the setup's working.
 */
@ExcludeFromTree
public class MoveMDO extends MainSchema {
	
	@Desc("Cooldown time - in seconds")
	public Float cooldown;

}
