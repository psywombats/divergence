/**
 *  ArchetypeEntryMDO.java
 *  Created on Oct 22, 2013 3:33:39 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * A name of an archetype.
 */
public class ArchetypeEntryMDO extends HeadlessSchema {
	
	@Desc("Full character name")
	public String fullname;
	
	@Desc("Character name in dialog")
	public String charaname;

}
