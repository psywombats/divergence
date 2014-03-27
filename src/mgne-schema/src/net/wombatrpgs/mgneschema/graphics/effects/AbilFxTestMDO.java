/**
 *  FxTextMDO.java
 *  Created on Oct 18, 2013 6:59:40 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.graphics.effects;

import net.wombatrpgs.mgneschema.graphics.effects.data.AbilFxMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * One of the dumbest graphical effects of all time.
 */
@Path("graphics/effects/")
public class AbilFxTestMDO extends AbilFxMDO {
	
	private static final long serialVersionUID = 1L;
	@Desc("Graphic to display on top of actor")
	@FileLink("textures")
	public String graphic;

}
