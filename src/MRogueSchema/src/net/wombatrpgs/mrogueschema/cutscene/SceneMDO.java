/**
 *  SceneMDO.java
 *  Created on Oct 22, 2013 1:21:42 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.TriggerRepeatType;

/**
 * A scene singleton.
 */
@Path("cutscene/")
public class SceneMDO extends SceneParentMDO {
	
	@Desc("The file with the scene data")
	@FileLink("scenes")
	public String file;
	
	@Desc("Repeat allowed?")
	public TriggerRepeatType repeat;

}
