/**
 *  DummyAttackMDO.java
 *  Created on Jun 16, 2013 12:12:46 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;

/**
 * Dummy move that magically refactored into the king of all moves! This is a
 * middleclass between attacks and moves. Things that implement it directly are
 * probably just basic attacks that rely on standard things like knockback and
 * stun to determine what they do. Special attacks should extend this.
 */
@Path("characters/hero/moveset/")
public class AttackMDO extends MoveMDO {
	
	@Desc("Knockback - Velocity enemies pick up when hit by this attack, in " +
			"px/s")
	public Integer kick;
	
	@Desc("Stun - How long enemies are incapable of accelerating/attacking " +
			"after being hit with this attack")
	public Float stun;

}
