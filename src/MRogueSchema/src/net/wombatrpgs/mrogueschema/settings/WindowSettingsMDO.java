/**
 *  MonsterMDO.java
 *  Created on Aug 7, 2012 2:21:51 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * All the static information about the window.
 */
@Path("settings/")
public class WindowSettingsMDO extends MainSchema {
	
	@Desc("Window title")
	public String windowName;
	
	@Desc("Width of in-game viewport, in game pixels")
	public Integer width;
	@Desc("Height of in-game viewport, in game pixels")
	public Integer height;
	
	@Desc("Final resolution width, in physical pixels")
	public Integer resWidth;
	@Desc("Final resolution height, in physical pixels")
	public Integer resHeight;

}
