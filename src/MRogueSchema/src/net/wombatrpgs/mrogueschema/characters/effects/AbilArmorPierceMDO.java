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
 * Some damage that ignores armor. Nice for spells.
 */
@Path("characters/effects/")
public class AbilArmorPierceMDO extends AbilityEffectMDO {
	
	@Desc("Percentage of armor to ignore, as a float% (0-1, 1 is full pierce)")
	public Float pierce;

}
