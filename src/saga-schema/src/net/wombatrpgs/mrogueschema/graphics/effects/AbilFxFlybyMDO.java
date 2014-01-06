/**
 *  AbilFxFlyby.java
 *  Created on Oct 27, 2013 11:40:22 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.graphics.effects.data.AbilFxMDO;

/**
 * Lucifer error box attack!
 */
@Path("graphics/effects/")
public class AbilFxFlybyMDO extends AbilFxMDO {
	
	@Desc("Graphic to use for the flyby")
	@FileLink("sprites")
	public String graphic;
	
	@Desc("Count - how many to spawn")
	public Integer count;
	
	@Desc("Spread x - variance in starting x-coord, in pixels")
	public Integer spreadX;
	
	@Desc("Spread y - variance in starting y-coord, in pixels")
	public Integer spreadY;

}
