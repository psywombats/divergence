/**
 *  EnemyEventMDO.java
 *  Created on Jan 23, 2013 9:37:59 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.characters.ai.data.IntelligenceMDO;
import net.wombatrpgs.mrogueschema.characters.data.CharacterMDO;

/**
 * A thing on the ground that attempts to killlll youuuu.
 */
@Path("characters/")
public class EnemyMDO extends CharacterMDO {
	
	@Desc("Intelligence - the set of behaviors that control the enemy")
	@SchemaLink(IntelligenceMDO.class)
	public String intelligence;
	
	@Desc("Danger level - minimum danger on which this enemy spawns")
	public Integer danger;

}
