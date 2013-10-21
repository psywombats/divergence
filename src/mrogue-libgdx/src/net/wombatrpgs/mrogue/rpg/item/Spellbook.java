/**
 *  ItemSpellbook.java
 *  Created on Oct 20, 2013 6:53:26 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import net.wombatrpgs.mrogueschema.items.SpellbookMDO;

/**
 * It's a thing you learn an ability. RPGs have been around for like 30 years
 * dude.
 */
public class Spellbook extends Item {
	
	protected SpellbookMDO mdo;

	/**
	 * Creates a spellbook from data.
	 * @param	mdo				The data to generate from
	 */
	public Spellbook(SpellbookMDO mdo) {
		super(mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.item.Item#internalUse()
	 */
	@Override
	protected void internalUse() {
		// TODO spellbook
	}

}
