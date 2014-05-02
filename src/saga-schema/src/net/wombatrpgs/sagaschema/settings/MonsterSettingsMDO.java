/**
 *  MonsterSettingsMDO.java
 *  Created on May 2, 2014 7:20:10 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Settings for random encounters, meat abundancy, transformations, etc
 */
@Path("settings/")
public class MonsterSettingsMDO extends MainSchema {
	
	@Desc("Meat abundancy - chance that a group drops meat from 0 to 100")
	public Integer meatChance;

}
