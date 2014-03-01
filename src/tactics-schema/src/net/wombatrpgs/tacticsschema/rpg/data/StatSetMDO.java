/**
 *  Stats.java
 *  Created on Feb 12, 2014 2:13:42 AM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * A set of changes to be made to a set of stats.
 */
public class StatSetMDO extends HeadlessSchema {
	
	@Desc("Stat modifiers")
	@InlineSchema(StatEntryMDO.class)
	public StatEntryMDO stats[];
	
	@Desc("Flags")
	@InlineSchema(FlagEntryMDO.class)
	public FlagEntryMDO flags[];

}
