/**
 *  EnemyEventMDO.java
 *  Created on Jan 23, 2013 9:37:59 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.enemies;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.data.IntelligenceMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.EmitterMDO;
import net.wombatrpgs.rainfallschema.graphics.GibsetMDO;

/**
 * A thing on the ground that attempts to killlll youuuu.
 */
@Path("characters/enemies/")
public class EnemyEventMDO extends CharacterEventMDO {
	
	@Desc("Intelligence - the set of behaviors that control the enemy")
	@SchemaLink(IntelligenceMDO.class)
	public String intelligence;
	
	@Desc("Moveset - all moves this enemy can use")
	@SchemaLink(MoveMDO.class)
	public String[] moveset;
	
	@Desc("Vulnerability - describes what this enemy can be killed by")
	@SchemaLink(VulnerabilityMDO.class)
	public String vulnerability;
	
	@Desc("Gibset - the particles this enemy spews on death")
	@SchemaLink(GibsetMDO.class)
	@Nullable
	public String gibset;
	
	@Desc("Emitter - controls the emitted particles for this enemy")
	@SchemaLink(EmitterMDO.class)
	@Nullable
	public String emitter;
	
	@Desc("Death animation - this animation plays where the enemy dies")
	@SchemaLink(AnimationMDO.class)
	@Nullable
	public String dieAnim;

}
