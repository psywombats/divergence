/**
 *  TitleSettingsMDO.java
 *  Created on Oct 23, 2013 2:37:45 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Settings for the title screen.
 */
@Path("settings/")
public class TitleSettingsMDO extends MainSchema {
	
	@Desc("Title image")
	@FileLink("ui")
	public String bg;
	
	@Desc("Prompt")
	@FileLink("ui")
	public String prompt;
	
	@Desc("Prompt x - pixels from bottom left to draw prompt at")
	public Integer promptX;
	
	@Desc("Prompt x - pixels from bottom left to draw prompt at")
	public Integer promptY;
	
	@Desc("Prompt cycle time, in s")
	public Float cycle;

}
