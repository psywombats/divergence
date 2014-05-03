/**
 *  CharacterMDO.java
 *  Created on Apr 2, 2014 9:41:38 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara;

import net.wombatrpgs.mgneschema.graphics.FourDirMDO;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Gender;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.stats.StatSetMDO;

/**
 * A playable character, or at least a blank, recruitable one.
 */
@Path("rpg/")
public class CharaMDO extends MainSchema {
	
	@Desc("Name - default in the case of players, or else enemy name")
	public String name;
	
	@Desc("Gender")
	@DefaultValue("NONE")
	public Gender gender;
	
	@Desc("Race")
	public Race race;
	
	@Desc("Family - used for transformations, only monsters should have this")
	@SchemaLink(MonsterFamilyMDO.class)
	@Nullable
	public String family;
	
	@Desc("Species - the name of the specific monster subtype, eg GOBLIN, "
			+ "only monsters should have this")
	@DefaultValue("")
	public String species;
	
	@Desc("Appearance")
	@SchemaLink(FourDirMDO.class)
	public String appearance;
	
	@Desc("In-battle portrait")
	@DefaultValue("None")
	@FileLink("sprites")
	@Nullable
	public String portrait;
	
	@Desc("Meat eat level - the power of the meat this character drops, "
			+ "compared to the transform level of other monsters, monster only")
	public Integer meatEatLevel;
	
	@Desc("Meat transform level - the power of meat needed to transform into "
			+ "this character, compared to the eat level of others")
	public Integer meatTargetLevel;
	
	@Desc("GP - dropped when this character is defeated by the player")
	@DefaultValue("0")
	public Integer gp;
	
	@Desc("Equipped items/abilities")
	@SchemaLink(CombatItemMDO.class)
	public String equipped[];
	
	@InlineSchema(StatSetMDO.class)
	public StatSetMDO stats;

}
