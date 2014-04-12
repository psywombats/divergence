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
public class CharaInventory {
	
	public static final int SLOT_COUNT = 8;
	
	protected Chara chara;
	protected CombatItem[] items;
	protected int equipCount;
	protected int abilCount;
	
	/**
	 * Creates a new starter inventory suitable for the given character data.
	 * @param	mdo				The data to create from
	 */
	public CharaInventory(CharacterMDO mdo, Chara chara) {
		this.chara = chara;
		items = new CombatItem[SLOT_COUNT];
		for (int i = 0; i < mdo.equipped.length; i += 1) {
			String key = mdo.equipped[i];
			CombatItem item = new CombatItem(key);
			item.setOwner(chara);
			items[i] = item;
		}
	}
	
	/**
	 * Returns the item located at the designated slot. Returns null if nothing
	 * there.
	 * @param	slot			The slot to fetch item from
	 * @return					The item in that slot
	 */
	public CombatItem at(int slot) {
		return items[slot];
	}
	
	/**
	 * Determines if the designated slot could potentially have an equippable
	 * item put in that slot.
	 * @param	slot			The slot to check
	 * @return					True if an item could be put there
	 */
	public boolean equippableAt(int slot) {
		switch (chara.getRace()) {
		case HUMAN: case ROBOT:
			return true;
		case MUTANT:
			return slot >= 4;
		case MONSTER:
			return false;
		default:
			MGlobal.reporter.warn("Unknown race " + chara.getRace());
			return false;
		}
	}

}
