/**
 *  MonsterFamilyMDO.java
 *  Created on May 2, 2014 6:55:23 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.chara.data.TransformationMDO;

/**
 * A group of monsters that share battle sprites and overall design.
 */
@Path("rpg/")
public class MonsterFamilyMDO extends MainSchema {
	
	@Desc("Transformations to other monster families")
	@InlineSchema(TransformationMDO.class)
	public TransformationMDO[] transformations;

}
