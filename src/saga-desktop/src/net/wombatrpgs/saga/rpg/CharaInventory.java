/**
 *  Inventory.java
 *  Created on Apr 12, 2014 3:00:32 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.sagaschema.rpg.chara.CharacterMDO;

/**
 * A collection of combat items worn by a character.
 */
public class CharaInventory extends Inventory {
	
	protected static final int SLOT_COUNT = 8;
	
	protected Chara chara;
	
	/**
	 * Creates a new starter inventory suitable for the given character data.
	 * @param	mdo				The data to create from
	 */
	public CharaInventory(CharacterMDO mdo, Chara chara) {
		super(SLOT_COUNT);
		this.chara = chara;
		for (int i = 0; i < mdo.equipped.length; i += 1) {
			String key = mdo.equipped[i];
			CombatItem item = new CombatItem(key);
			item.setOwner(chara);
			set(i, item);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.Inventory#reservedAt(int)
	 */
	@Override
	public boolean reservedAt(int slot) {
		switch (chara.getRace()) {
		case HUMAN: case ROBOT: case MONSTER:
			return false;
		case MUTANT:
			return slot <= 4;
		default:
			MGlobal.reporter.warn("Unknown race " + chara.getRace());
			return false;
		}
	}

}
