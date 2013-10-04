/**
 *  SceneMDO.java
 *  Created on Feb 3, 2013 8:10:29 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.cutscene.data.TriggerRepeatType;

/**
 * A scene. Used to link a scene file to a database key.
 */
@Path("cutscene/")
public class SceneMDO extends MainSchema {
	
	@Desc("File - the actual .scene file where the cutscene data is held")
	@FileLink("scenes")
	public String file;
	
	@Desc("Repeat type - should this cutscene be played more than once?")
	@DefaultValue("RUN_ONLY_ONCE")
	public TriggerRepeatType repeat;

}
