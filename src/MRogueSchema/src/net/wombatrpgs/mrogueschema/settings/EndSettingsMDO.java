/**
 *  EndSettingsMDO.java
 *  Created on Oct 29, 2013 9:35:02 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;

/**
 * Endgame
 */
@Path("settings/")
public class EndSettingsMDO extends MainSchema {
	
	@Desc("Title image")
	@FileLink("ui")
	public String bg;
	
	@Desc("Ending cutscene")
	@SchemaLink(SceneParentMDO.class)
	public String ending;

}
