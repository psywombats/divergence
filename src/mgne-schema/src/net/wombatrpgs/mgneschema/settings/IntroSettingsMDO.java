/**
 *  IntroSettingsMDO.java
 *  Created on Feb 22, 2013 4:36:25 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * What happens when the game starts.
 */
@Path("settings/")
public class IntroSettingsMDO extends MainSchema {
	
	@Desc("Map - the map that things open on, usually a blank screen with the hero on it")
	@FileLink("maps")
	public String map;
	
	@Desc("Start x - tile on the map where hero starts, x-coord")
	public Integer mapX;
	
	@Desc("Start y - tile on the map where the hero starts, y-coord")
	public Integer mapY;
	
	@Desc("Title bg - title background image")
	@FileLink("ui")
	public String titleBG;

}
