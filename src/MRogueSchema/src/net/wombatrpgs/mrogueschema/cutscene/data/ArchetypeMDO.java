/**
 *  ArchetypeEntryMDO.java
 *  Created on Oct 22, 2013 3:07:24 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * Name of a character. EOM. NOT ANYMORE.
 */
public class ArchetypeMDO extends HeadlessSchema {
	
	@Desc("Archetype name, etc HERO, TOOL, etc")
	public String charname;
	
	@InlineSchema(ArchetypeEntryMDO.class)
	public ArchetypeEntryMDO entries[];

}
