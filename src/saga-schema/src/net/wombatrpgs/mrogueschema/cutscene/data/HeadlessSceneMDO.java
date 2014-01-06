/**
 *  SceneMDO.java
 *  Created on Feb 3, 2013 8:10:29 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * A scene. Used to link a scene file to a database key.
 */
public class HeadlessSceneMDO extends HeadlessSchema {
	
	@Desc("File - the actual .scene file where the cutscene data is held")
	@FileLink("scenes")
	public String file;
	
}
