/**
 *  DeathSettingsMDO.java
 *  Created on Oct 23, 2013 5:37:49 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;

/**
 * What happens when you bite it.
 */
@Path("settings/")
public class DeathSettingsMDO extends MainSchema {
	
	@Desc("Drama scene - the scene to play when you croak")
	@SchemaLink(SceneParentMDO.class)
	public String scene;
	
	@Desc("Tech scene - the scene to play when you arrive on the go screen")
	@SchemaLink(SceneParentMDO.class)
	public String immScene;
	
	@Desc("Death image")
	@FileLink("ui")
	public String bg;

}
