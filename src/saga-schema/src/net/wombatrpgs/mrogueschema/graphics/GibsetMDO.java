/**
 *  GibsetMDO.java
 *  Created on Jan 31, 2013 9:54:08 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * A set of gibs to fly around when enemies get killed.
 */
@Path("graphics/")
public class GibsetMDO extends MainSchema {
	
	@Desc("Image file - gibset, should be png")
	@FileLink("sprites")
	public String file;
	
	@Desc("Gib count")
	public Integer count;
	
	@Desc("Gib width")
	public Integer frameWidth;
	
	@Desc("Gib height")
	public Integer frameHeight;

}
