package net.wombatrpgs.rainfallschema.characters.enemies.ai;
/**
 *  PlayerlikeBehaviorMDO.java
 *  Created on Jun 22, 2013 11:33:32 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */


import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.data.IntelligenceMDO;

/**
 * All ABS-participants, essentially.
 */
@Path("characters/enemies/")
public class PlayerlikeIntelligenceMDO extends IntelligenceMDO {
	
	@Desc("Vision - How far away this enemy can see, in px")
	public Integer visionRadius;
	
	@Desc("Far range - Maximum bound of comfortable range from hero, in px")
	public Integer farRange;
	
	@Desc("Near range - Minimum bound of comfortable range from hero, in px")
	public Integer nearRange;
	
	//@Desc

}
