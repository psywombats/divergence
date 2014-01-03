/**
 *  GeneratedPotion.java
 *  Created on Oct 22, 2013 5:22:28 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.items.GeneratedSpellbookMDO;
import net.wombatrpgs.mrogueschema.items.SpellbookMDO;
import net.wombatrpgs.mrogueschema.items.SpellbookPrefixMDO;
import net.wombatrpgs.mrogueschema.items.SpellbookTypeMDO;

/**
 * A spellbook that was pieced together from the spellbook generator.
 */
public class GeneratedSpellbook extends Spellbook {
	
	protected SpellbookPrefixMDO preMDO;
	protected SpellbookTypeMDO typeMDO;

	/**
	 * Generates a potion randomly from data.
	 * @param	mdo				The data to generate from
	 */
	public GeneratedSpellbook(SpellbookPrefixMDO preMDO, SpellbookTypeMDO typeMDO) {
		super(generateMDO(preMDO, typeMDO), generateAbilMDO(preMDO, typeMDO));
		this.preMDO = preMDO;
		this.typeMDO = typeMDO;
	}
	
	/**
	 * A constructor standin. Creates a generated spellbook from random data.
	 * @param	mdo				The data to use to generate
	 * @return					The generated spellbook
	 */
	public static GeneratedSpellbook construct(GeneratedSpellbookMDO mdo) {
		SpellbookPrefixMDO preMDO = MGlobal.data.getEntryFor(
				mdo.prefixes[MGlobal.rand.nextInt(mdo.prefixes.length)],
				SpellbookPrefixMDO.class);
		SpellbookTypeMDO typeMDO = MGlobal.data.getEntryFor(
				mdo.types[MGlobal.rand.nextInt(mdo.types.length)],
				SpellbookTypeMDO.class);
		typeMDO.icon = mdo.icon;
		return new GeneratedSpellbook(preMDO, typeMDO);
	}
	
	/**
	 * Generates the mdo needed for a spellbook.
	 * @param	preMDO			The data for the prefix
	 * @param	typeMDO			The data for the type
	 * @return					The mdo that sort of looks like that
	 */
	protected static SpellbookMDO generateMDO(SpellbookPrefixMDO preMDO,
			SpellbookTypeMDO typeMDO) {
		SpellbookMDO mdo = new SpellbookMDO();	
		mdo.gameDesc = preMDO.effectDesc + " " + typeMDO.typeDesc;
		mdo.icon = typeMDO.icon;
		mdo.name = "a book of " + preMDO.effectName + " " + typeMDO.typeName;
		mdo.rarity = 0f;
		return mdo;
	}
	
	/**
	 * Generates the ability mdo needed for a spellbook.
	 * @param	preMDO			The data for the prefix
	 * @param	typeMDO			The data for the type
	 * @return					The mdo that sort of looks like that
	 */
	protected static AbilityMDO generateAbilMDO(SpellbookPrefixMDO preMDO,
			SpellbookTypeMDO typeMDO) {
		AbilityMDO abilMDO = new AbilityMDO();
		abilMDO.effect = preMDO.effect;
		abilMDO.energyCost = 1000;
		abilMDO.fx = preMDO.fx;
		abilMDO.icon = preMDO.icon;
		abilMDO.mpCost = Math.round(preMDO.cost * typeMDO.costMult);
		abilMDO.name = preMDO.effectName + " " + typeMDO.typeName;
		abilMDO.range = typeMDO.range;
		abilMDO.target = typeMDO.target;
		return abilMDO;
	}

}
