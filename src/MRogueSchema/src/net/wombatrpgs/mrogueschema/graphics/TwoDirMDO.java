/**
 *  TwoDirMDO.java
 *  Created on Jan 24, 2013 6:20:15 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * Same thing as a FourDir, but moves in only two directions.
 */
@Path("graphics/")
public class TwoDirMDO extends DirMDO {
	
	@Desc("Right-facing animation")
	@SchemaLink(AnimationMDO.class)
	public String rightAnim;
	
	@Desc("Left-facing animation")
	@SchemaLink(AnimationMDO.class)
	public String leftAnim;

}
