/**
 *  MonsterTypeMDO.java
 *  Created on Oct 12, 2013 2:55:46 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;

/**
 * One of the archetypes from gdocs.
 */
public class MonsterNameMDO extends HeadlessSchema {
	
	@Desc("Monster name")
	public String typeName;
	
	@Desc("Archetype")
	@SchemaLink(EnemyMDO.class)
	public String archetype;

}
