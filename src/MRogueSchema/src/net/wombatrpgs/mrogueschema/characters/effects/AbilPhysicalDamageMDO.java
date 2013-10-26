/**
 *  AbilArmorPierceMDO.java
 *  Created on Oct 18, 2013 4:18:01 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;

/**
 * Offensive melee
 */
@Path("characters/effects/")
public class AbilPhysicalDamageMDO extends AbilityEffectMDO {
	
	@Desc("Multiplier to deal of normal melee damage")
	public Float mult;
	
	@Desc("Base damage to always deal")
	public Integer base;

}
