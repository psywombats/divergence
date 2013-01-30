/**
 *  DashSchema.java
 *  Created on Dec 12, 2012 4:39:45 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;

/**
 * Base class for all schema that form part of the moveset. Hopefully
 * inheritance works in this little setup. Shouldn't appear on the tree either,
 * assuming the setup's working.
 */
@ExcludeFromTree
public class MoveMDO extends MainSchema {
	
	@Desc("Cooldown time - in seconds")
	public Float cooldown;
	
	@Desc("4Dir Animation - played when move is in action")
	@SchemaLink(FourDirMDO.class)
	@Nullable
	public String animation;

}
