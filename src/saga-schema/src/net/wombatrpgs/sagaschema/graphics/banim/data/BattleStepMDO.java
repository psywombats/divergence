/**
 *  BattleStepMDO.java
 *  Created on May 22, 2014 10:23:53 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics.banim.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * A single command of a battle animation.
 */
public class BattleStepMDO extends HeadlessSchema {
	
	@Desc("Component sprite file")
	@FileLink("sprites")
	public String sprite;
	
	@Desc("Start - when this component appears, in s relative to beginning")
	public Float start;
	
	@Desc("Duration - how long this sprite appears, in s")
	public Float duration;
	
	@Desc("Appear x - x-coord the sprite's center appears at relative to the " +
			"center of the enemy battle portrait")
	public Float x;
	
	@Desc("Appear y - y-coord the sprite's center appears at relative to the " +
			"center of the enemy battle portrait")
	public Float y;

}
