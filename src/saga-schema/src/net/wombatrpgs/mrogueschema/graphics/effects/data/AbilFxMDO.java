/**
 *  AttackEffectMDO.java
 *  Created on Oct 18, 2013 3:48:14 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.effects.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;

/**
 * A shader and associated info for a special attack.
 */
@ExcludeFromTree
public class AbilFxMDO extends MainSchema {
	
	@Desc("Duration - how long this effect lasts for, in s")
	public Float duration;
	
	@Desc("Tracking - does this event follow the target around?")
	@DefaultValue("STATIONARY")
	public TrackingType tracking;
	
	@Desc("Z type - does this event appear above or below hero?")
	@DefaultValue("ABOVE")
	public ZType z;

}
