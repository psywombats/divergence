/**
 *  AbilityMDO.java
 *  Created on Feb 24, 2014 7:30:23 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.tacticsschema.rpg.abil.data.ProjectorType;
import net.wombatrpgs.tacticsschema.rpg.abil.data.WarheadMDO;

/**
 * MDO for actions.
 */
@Path("rpg/")
public class AbilityMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;

	@Desc("Ability name - displayed in-game")
	public String abilityName;
	
	@Desc("Targeting type")
	@DefaultValue("MELEE")
	public ProjectorType projector;
	
	@Desc("Range - in tiles, 0 for self, 1 for melee")
	@DefaultValue("1")
	public Integer range;
	
	@Desc("Warhead - corresponds to an in-code definition for an effect")
	@SchemaLink(WarheadMDO.class)
	public String warhead;

}
