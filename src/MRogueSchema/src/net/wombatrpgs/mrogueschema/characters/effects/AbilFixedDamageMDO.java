/**
 *  AbilFixedDamageMDO.java
 *  Created on Oct 29, 2013 3:59:01 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;

/**
 * Inflicts a set amount of damage on the victim. Also useful for healing.
 */
@Path("characters/effects/")
public class AbilFixedDamageMDO extends AbilityEffectMDO {
	
	@Desc("Damage - will deal exactly this amount of damage, negative for healing")
	public Integer damage;

}
