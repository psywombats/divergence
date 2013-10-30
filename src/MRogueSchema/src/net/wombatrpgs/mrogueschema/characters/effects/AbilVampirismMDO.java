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
 * Offensive bite!
 */
@Path("characters/effects/")
public class AbilVampirismMDO extends AbilityEffectMDO {
	
	@Desc("Multiplier to deal of normal phys damage")
	public Float physMult;
	
	@Desc("Multiplier to deal of normal magic damage")
	public Float magMult;
	
	@Desc("Base damage to always deal")
	public Integer base;
	
	@Desc("Percent of damage that user should be healed")
	public Float healMult;

}
