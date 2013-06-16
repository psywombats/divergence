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
 * D-d-d-dummy move. Make this extend something. Actually, just delete it.
 */
@Path("characters/hero/moveset/")
public class DummyAttackMDO extends MoveMDO {
	
	@Desc("Knockback")
	public Integer kick;
	
	@Desc("Stun")
	public Integer stun;

}
