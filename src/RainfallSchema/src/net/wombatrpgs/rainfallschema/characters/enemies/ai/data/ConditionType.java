/**
 *  Condition.java
 *  Created on Jan 24, 2013 12:03:12 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.enemies.ai.data;

import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;

/**
 * A condition is part of an intent. If the condition holds, the intelligence
 * will carry out the action part of this intent.
 */
@ExcludeFromTree
public enum ConditionType {
	
	HERO_IN_VISIONCONE,
	HERO_IN_RANGE,
	DEFAULT,

}
