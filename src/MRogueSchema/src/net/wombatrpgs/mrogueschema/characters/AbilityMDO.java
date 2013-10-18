/**
 *  SpecialAttack.java
 *  Created on Oct 18, 2013 3:49:41 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * Something a little more subtle than walking into a character.
 */
@Path("characters/")
public class AbilityMDO extends MainSchema {
	
	@Desc("Name - displayed in-game")
	public String name;
	
	@Desc("Target - What or where this ability targets")
	public AbilityTargetType target;
	
	@Desc("Effect - The code operating behind this ability")
	@SchemaLink(AbilityEffectMDO.class)
	public String effect;
	
	@Desc("Energy cost - How long it takes to use this action (1000 default, 2000 is twice as long, etc")
	@DefaultValue("1000")
	public Integer energyCost;
	
	@Desc("Resource cost - How much mp/mana/sp/stamina etc this takes")
	@DefaultValue("0")
	public Integer mpCost;

}
