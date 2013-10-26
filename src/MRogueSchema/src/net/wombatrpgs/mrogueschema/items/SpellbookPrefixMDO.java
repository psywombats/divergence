/**
 *  SpellbookPrefix.java
 *  Created on Oct 26, 2013 3:03:33 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.items;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.AbilFxMDO;

/**
 * Determines the effect of a spellbook.
 */
@Path("items/")
public class SpellbookPrefixMDO extends MainSchema {
	
	@Desc("Name - part of the in-game name, for instance 'fire' or 'sword'")
	public String effectName;
	
	@Desc("Description - half of in-game description")
	public String effectDesc;
	
	@Desc("Effect")
	@SchemaLink(AbilityEffectMDO.class)
	public String effect;
	
	@Desc("Base MP cost - will get multiplied by the type's multiplier")
	public Float cost;
	
	@Desc("Icon - file used for this ability in the UI")
	@FileLink("ui")
	@Nullable
	public String icon;
	
	@Desc("Graphical fx - Special animations for this ability!!")
	@SchemaLink(AbilFxMDO.class)
	@Nullable
	public String fx;

}
