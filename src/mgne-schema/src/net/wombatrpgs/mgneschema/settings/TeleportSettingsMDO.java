/**
 *  TeleportSettigsMDO.java
 *  Created on Feb 8, 2013 12:05:33 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Teleport pre/post scripts
 */
@Path("settings/")
public class TeleportSettingsMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;

	@Desc("Pre-scene - will play before all teleports, shouldn't contain event references")
	@FileLink("scenes")
	public String pre;
	
	@Desc("Post-scene - will play after all teleports, shouldn't contain event references")
	@FileLink("scenes")
	public String post;

}
