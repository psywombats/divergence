/**
 *  EnemyEventMDO.java
 *  Created on Jan 23, 2013 9:37:59 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.enemies;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.enemies.ai.IntelligenceMDO;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * A thing on the ground that attempts to killlll youuuu.
 */
@Path("enemies/")
public class EnemyEventMDO extends CharacterEventMDO {
	
	@Desc("Intelligence - the set of behaviors that control the enemy")
	@SchemaLink(IntelligenceMDO.class)
	String intelligence;	

}
