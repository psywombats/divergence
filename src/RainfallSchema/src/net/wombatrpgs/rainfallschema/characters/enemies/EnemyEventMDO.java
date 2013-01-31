/**
 *  EnemyEventMDO.java
 *  Created on Jan 23, 2013 9:37:59 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.enemies;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.IntelligenceMDO;

/**
 * A thing on the ground that attempts to killlll youuuu.
 */
@Path("characters/enemies/")
public class EnemyEventMDO extends CharacterEventMDO {
	
	@Desc("Intelligence - the set of behaviors that control the enemy")
	@SchemaLink(IntelligenceMDO.class)
	public String intelligence;	
	
	@Desc("Vulnerability - describes what this enemy can be killed by")
	@SchemaLink(VulnerabilityMDO.class)
	public String vulnerability;

}
