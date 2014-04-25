/**
 *  NumericStatSetMDO.java
 *  Created on Apr 25, 2014 3:46:39 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.stats;

import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;

/**
 * A list of only-numeric stats.
 */
public class NumericStatModMDO extends HeadlessSchema {
	
	@InlineSchema(StatEntryMDO.class)
	public StatEntryMDO stats[];
	
}
