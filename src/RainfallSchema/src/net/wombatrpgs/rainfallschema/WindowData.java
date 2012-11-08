/**
 *  MonsterMDO.java
 *  Created on Aug 7, 2012 2:21:51 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * All the static information about the window.
 */
@Path("settings/")
public class WindowData extends MainSchema {
	
	@Desc("Window title")
	public String windowName;

}
