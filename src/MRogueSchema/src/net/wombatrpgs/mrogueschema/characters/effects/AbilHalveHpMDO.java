/**
 *  AbilHalveHP.java
 *  Created on Oct 18, 2013 9:12:49 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.effects;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;

/**
 * Halves the HP of whatever it hits.
 */
@Path("characters/effects/")
public class AbilHalveHpMDO extends AbilityEffectMDO {
	
	@Desc("What percentage of HP to deduct, as a 0-1 fraction")
	@DefaultValue(".5")
	public Float factor;
	
	@Desc("Max possible damage this attack can cause")
	public Integer maxDamage;

}
