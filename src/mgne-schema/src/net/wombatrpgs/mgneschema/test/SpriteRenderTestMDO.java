/**
 *  SpriteRenderTestMDO.java
 *  Created on Nov 11, 2012 2:20:19 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.test;

import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Debug.
 */
@Path("test/")
public class SpriteRenderTestMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;
	@Desc("The animation to play on the test map")
	@SchemaLink(AnimationMDO.class)
	public String anim;

}
