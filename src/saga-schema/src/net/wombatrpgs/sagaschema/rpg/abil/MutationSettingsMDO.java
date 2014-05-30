/**
 *  MutantAbilListMDO.java
 *  Created on May 24, 2014 7:30:46 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.abil.data.MutantAbilMDO;

/**
 * A bunch of abilities that mutants can learn at differing probabilities.
 */
@Path("rpg/")
public class MutationSettingsMDO extends MainSchema {
	
	@Desc("Available mutant abilities")
	@InlineSchema(MutantAbilMDO.class)
	public MutantAbilMDO[] abils;
	
	@Desc("Mutation chance - probability 0-100 that mutants mutate after a battle")
	public Integer mutationChance;
	
	@Desc("Min HP - minimum health gained in an MHP level up")
	public Integer minHealth;
	
	@Desc("Max HP - maximum health gained in an MHP level up")
	public Integer maxHealth;
	
	@Desc("HP weight - weight of an MHP gain relative to other gains")
	public Integer weightHp;
	
	@Desc("STR weight - weight of a STR gain relative to other gains")
	public Integer weightStr;
	
	@Desc("DEF weight - weight of a DEF gain relative to other gains")
	public Integer weightDef;
	
	@Desc("AGI weight - weight of an AGI gain relative to other gains")
	public Integer weightAgi;
	
	@Desc("MAN weight - weight of a MAN gain relative to other gains")
	public Integer weightMana;
	
	@Desc("Ability weight - weight of learning an ability relative to other gains")
	public Integer weightAbil;

}
