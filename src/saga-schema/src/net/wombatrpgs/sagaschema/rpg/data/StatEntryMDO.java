/**
 *  StatEntryMDO.java
 *  Created on Feb 28, 2014 6:50:29 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.data;

import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * A single stat:value pair that composes a statset.
 */
public class StatEntryMDO extends HeadlessSchema {
	
	public Stat stat;
	
	public Float value;

}
