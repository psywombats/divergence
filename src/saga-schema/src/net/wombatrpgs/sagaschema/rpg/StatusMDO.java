/**
 *  StatusMDO.java
 *  Created on Apr 30, 2014 2:20:11 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.data.LethalityType;
import net.wombatrpgs.sagaschema.rpg.data.RecoverType;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Status condition.
 */
@Path("rpg/")
public class StatusMDO extends MainSchema {
	
	@Desc("Full name - used in extended menus, eg BLIND")
	public String fullName;
	
	@Desc("Short name - used in small menus, eg BLND")
	public String tag;
	
	@Desc("Inflict message - printed when inflicted, eg \" is blind\"")
	public String inflictString;
	
	@Desc("Heal message - printed when healed, eg \" regains sight\"")
	public String healString;
	
	@Desc("Priority - effect with higher priority overwrite other effects")
	@DefaultValue("50")
	public Integer priority;
	
	@Desc("Resist flag - characters with this flag are immune")
	@Nullable
	public Flag resistFlag;
	
	@Desc("Prevent action chance - chance from 0 to 100 chara will be unable to act in battle")
	@DefaultValue("0")
	public Integer preventChance;
	
	@Desc("Random action chance - chance from 0 to 100 chara will pick random move and target")
	@DefaultValue("0")
	public Integer randomChance;
	
	@Desc("Recover chance - chance from 0 to 100 chara will randomly heal this status in battle")
	@DefaultValue("0")
	public Integer recoverChance;
	
	@Desc("Lethality type - the game could treat this status the same as being dead")
	@DefaultValue("NON_DEADLY")
	public LethalityType lethality;
	
	@Desc("Recovery type")
	public RecoverType recover;
	
	@Desc("Reduced stats - in-battle values is reduced by 50% for these stats in battle")
	public Stat[] halvedStats;

}
