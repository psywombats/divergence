/**
 *  DirectionSelectorMDO.java
 *  Created on Feb 26, 2014 10:27:21 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Request direction from player.
 */
@Path("ui/")
public class DirectionSelectorMDO extends MainSchema {
	
	@Desc("Four dir selector - overlay displays when requesting 4dir")
	@FileLink("ui")
	public String orthoDir;
	
	@Desc("Eight dir selector - overlay displays when requesting 8dir")
	@FileLink("ui")
	public String eightTarget;

}
