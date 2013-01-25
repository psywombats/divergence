/**
 *  IntentMDO.java
 *  Created on Jan 23, 2013 11:58:41 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.enemies.ai.intent;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.rainfallschema.enemies.ai.data.ActionType;
import net.wombatrpgs.rainfallschema.enemies.ai.data.ConditionType;

/**
 * An intent is something an ai wants to do if certain conditions are met. A
 * bunch of them stacked together forms an intellect. Feel free to override this
 * with whatever is necessary for an individual intent.
 */
@ExcludeFromTree
public class IntentMDO extends HeadlessSchema {
	
	@Desc("Action - will do this if condition is met")
	@DefaultValue("SIT_STILL")
	public ActionType action;
	
	@Desc("Condition - will action is this is met")
	@DefaultValue("DEFAULT")
	public ConditionType condition;
	
	@Desc("Priority - which action to do first (0 is lowest, 100 etc is high)")
	public Integer priority;

}
