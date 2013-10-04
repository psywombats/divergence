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

/**
 * A thing on the ground that attempts to killlll youuuu.
 */
@Path("characters/enemies/")
public class EnemyEventMDO extends CharacterEventMDO {
	
	@Desc("Intelligence - the set of behaviors that control the enemy")
	@SchemaLink(IntelligenceMDO.class)
	public String intelligence;
	
//	@Desc("Gibset - the particles this enemy spews on death")
//	@SchemaLink(GibsetMDO.class)
//	@Nullable
//	public String gibset;
//	
//	@Desc("Emitter - controls the emitted particles for this enemy")
//	@SchemaLink(EmitterMDO.class)
//	@Nullable
//	public String emitter;
//	
//	@Desc("Death animation - this animation plays where the enemy dies")
//	@SchemaLink(AnimationMDO.class)
//	@Nullable
//	public String dieAnim;

}
