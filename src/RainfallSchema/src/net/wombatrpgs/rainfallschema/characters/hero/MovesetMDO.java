/**
 *  MovesetSchema.java
 *  Created on Dec 14, 2012 11:48:36 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero;

import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.characters.hero.data.MovesetEntryMDO;

/**
 * A container for all the hero's moves in one convenient blob. It's useful to
 * have multiple sets for balancing, so that one could be swapped in and out.
 */
@Path("characters/hero/")
public class MovesetMDO extends MainSchema {
	
	@InlineSchema(MovesetEntryMDO.class)
	public MovesetEntryMDO[] moves;

}
