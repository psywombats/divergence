/**
 *  MonsterMDO.java
 *  Created on Aug 7, 2012 2:21:51 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.settings;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * All the static information about the window.
 */
@Path("settings/")
public class WindowDataMDO extends MainSchema {
	
	@Desc("Window title")
	public String windowName;
	
	@Desc("Default width of window, in pixels")
	public Integer defaultWidth;
	@Desc("Default height of window, in pixels")
	public Integer defaultHeight;

}
