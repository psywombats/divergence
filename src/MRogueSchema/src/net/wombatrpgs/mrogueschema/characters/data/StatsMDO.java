/**
 *  StatsMDO.java
 *  Created on Aug 19, 2013 5:26:56 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * To correspond to the old stats object from the roguey engine
 */
@Path("characters/")
public class StatsMDO extends HeadlessSchema {
	
	@Desc("Max HP")
	public Integer mhp;
	
	@Desc("Speed - most things should have speed 100")
	@DefaultValue("100")
	public Integer speed;
	
	@Desc("Vision radius - in tiles")
	@DefaultValue("6")
	public Integer vision;

}
