/**
 *  EnemyModEntryMDO.java
 *  Created on Oct 29, 2013 5:37:41 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mrogueschema.characters.EnemyModMDO;

/**
 * That thing for enemies that generates them. Usually generated.
 */
public class EnemyModEntryMDO extends HeadlessSchema {
	
	@Desc("Modifier name")
	public String modName;
	
	@Desc("MDO associated with the entry, or null for cosmetic only")
	@SchemaLink(EnemyModMDO.class)
	@Nullable
	public String modMDO;

}
