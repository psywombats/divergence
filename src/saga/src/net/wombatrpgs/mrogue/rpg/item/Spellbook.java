/**
 *  ItemSpellbook.java
 *  Created on Oct 20, 2013 6:53:26 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.items.SpellbookMDO;

/**
 * It's a thing you learn an ability. RPGs have been around for like 30 years
 * dude.
 */
public class Spellbook extends Item {
	
	protected SpellbookMDO mdo;
	protected AbilityMDO abilMDO;

	/**
	 * Creates a spellbook from data, and will look up its ability later.
	 * @param	mdo				The data to generate from
	 */
	public Spellbook(SpellbookMDO mdo) {
		super(mdo);
		this.mdo = mdo;
	}
	
	/**
	 * Creates a spellbook from data, but will not use the ability field.
	 * @param	mdo				The data to generate from
	 * @param	abilMDO			The ability data to use
	 */
	protected Spellbook(SpellbookMDO mdo, AbilityMDO abilMDO) {
		this(mdo);
		this.abilMDO = abilMDO;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.item.Item#internalUse()
	 */
	@Override
	protected void internalUse() {
		if (owner.getAbilities().size() >= 6) {
			MGlobal.ui.getNarrator().msg("Sorry, your brain is full.");
			return;
		}
		if (abilMDO == null) {
			abilMDO = MGlobal.data.getEntryFor(mdo.ability, AbilityMDO.class);
		}
		Ability abil = new Ability(owner.getParent(), abilMDO);
		abil.queueRequiredAssets(MGlobal.assetManager);
		MGlobal.assetManager.finishLoading();
		abil.postProcessing(MGlobal.assetManager, 0);
		owner.getAbilities().add(abil);
		MGlobal.ui.getNarrator().msg(owner.getName() + " mastered " + abil.getName() + ".");
	}

}
