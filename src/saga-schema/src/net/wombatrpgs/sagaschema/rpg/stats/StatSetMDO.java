/**
 *  StatSet.java
 *  Created on Apr 2, 2014 9:48:59 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.stats;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * Headless stat grouping.
 */
public class StatSetMDO extends HeadlessSchema {
	
	@Desc("HP")
	public Integer hp;
	
	@Desc("MHP")
	public Integer mhp;
	
	@Desc("STR")
	public Integer str;
	
	@Desc("DEF")
	public Integer def;
	
	@Desc("AGI")
	public Integer agi;
	
	@Desc("MANA")
	public Integer mana;
	
	@Desc("Flags")
	public Flag[] flags;

}
