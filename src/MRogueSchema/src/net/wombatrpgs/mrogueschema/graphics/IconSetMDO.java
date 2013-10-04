/**
 *  IconSetMDO.java
 *  Created on Apr 2, 2013 2:31:46 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * Contains icons to use for a game.
 */
@Path("ui/")
public class IconSetMDO extends MainSchema {
	
	@Desc("16 * 16 icon")
	@FileLink("ui")
	public String icon16;
	
	@Desc("32 * 32 icon")
	@FileLink("ui")
	public String icon32;
	
	@Desc("128 * 128 icon")
	@FileLink("ui")
	public String icon128;

}
