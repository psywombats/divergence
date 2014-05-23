/**
 *  BattleAnimMDO.java
 *  Created on May 22, 2014 12:15:39 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics.banim;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleStepMDO;

/**
 * An animation for a battle or something like that? I don't know.
 */
@Path("graphics/")
public class BattleAnimStripMDO extends BattleAnimMDO {
	
	@Desc("Steps")
	@InlineSchema(BattleStepMDO.class)
	public BattleStepMDO[] steps;
	
}
