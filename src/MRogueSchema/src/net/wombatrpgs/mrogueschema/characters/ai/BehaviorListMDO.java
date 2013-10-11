/**
 *  BehaviorMDO.java
 *  Created on Jan 23, 2013 11:37:33 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.ai;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.ai.data.IntelligenceMDO;
import net.wombatrpgs.mrogueschema.characters.ai.intent.IntentMDO;

/**
 * An intelligence is a set of priorities that when placed together define how
 * an AI moves itself. This is currently limited to enemies though the system
 * is probably flexible enough to support any sort of state machine behavior.
 */
@Path("characters/ai/")
public class BehaviorListMDO extends IntelligenceMDO {
	
	@Desc("Intents - all actions the AI can take, but leave blank for ABS intelligence")
	@InlineSchema(IntentMDO.class)
	public IntentMDO[] intents;

}
