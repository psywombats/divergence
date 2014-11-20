/**
 *  Inventory.java
 *  Created on Apr 12, 2014 3:00:32 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;

/**
 * A collection of combat items worn by a character.
 */
public class CharaInventory extends Inventory {
	
	protected static final int SLOT_COUNT = 8;
	
	protected Chara chara;
	
	/**
	 * Creates a new starter inventory suitable for the given character data.
	 * @param	mdo				The data to create from
	 * @param	chara			The character to create for
	 */
	public CharaInventory(CharaMDO mdo, Chara chara) {
		super(SLOT_COUNT);
		this.chara = chara;
		for (int i = 0; i < mdo.equipped.length; i += 1) {
			String key = mdo.equipped[i];
			CombatItem item = new CombatItem(key);
			item.onAddedTo(this);
			set(i, item);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.items.Inventory#set
	 * (int, net.wombatrpgs.saga.rpg.items.CombatItem)
	 */
	@Override
	public CombatItem set(int slot, CombatItem item) {
		if (item != null) {
			chara.onEquip(item);
		}
		CombatItem old = super.set(slot, item);
		if (old != null) {
			chara.onUnequip(old);
		}
		return old;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.items.Inventory#reservedAt(int)
	 */
	@Override
	public boolean reservedAt(int slot) {
		switch (chara.getRace()) {
		case HUMAN: case ROBOT: case MONSTER:
			return false;
		case MUTANT:
			return slot < 4;
		default:
			MGlobal.reporter.warn("Unknown race " + chara.getRace());
			return false;
		}
	}
	
	/**
	 * @see net.wombatrpgs.saga.rpg.items.Inventory#regeneratesAt(int)
	 */
	@Override
	public boolean regeneratesAt(int n) {
		switch (chara.getRace()) {
		case HUMAN:
			return false;
		case MONSTER: case ROBOT:
			return true;
		case MUTANT:
			return n < 4;
		default:
			MGlobal.reporter.warn("Unknown race: " + chara.getRace());
			return false;
		}
	}
	
	/**
	 * Restores any abilities in this inventory to top form.
	 */
	public void restoreAbilUses() {
		for (int i = 0; i < capacity; i += 1) {
			if (regeneratesAt(i)) {
				restoreAt(i);
			}
		}
	}


	/**
	 * Checks if the given slot can have the given item stored in it. This
	 * checks for equipment exclusion flags as well as reserved slots
	 * @param	slot			The slot to check
	 * @param	item			The item to be equipped
	 * @return					True if the item can be equipped there
	 */
	public boolean canEquip(int slot, CombatItem item) {
		if (!equippableAt(slot)) return false;
		if (item == null) return true;
		if (chara.getRace() == Race.ROBOT && item.getUses() == 1) return false;
		for (int i = 0; i < capacity; i+= 1) {
			if (slot == i) continue;
			CombatItem other = get(i);
			if (other != null && other.sharesFlagWith(item)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the given slot can have an item stored in it. Does not imply
	 * that any old item can be put there.
	 * @param	slot			The slot to check
	 * @return					True if that slot can have an item, else false
	 */
	public boolean equippableAt(int slot) {
		if (reservedAt(slot)) {
			return false;
		} else if (chara.is(Flag.EQUIPMENT_FIX)) {
			return false;
		} else {
			return chara.getRace() != Race.MONSTER;
		}
	}
	
	/**
	 * Restores the ability at a given slot to max uses. Does nothing if no item
	 * at that location.
	 * @param	slot			The slot to restore at
	 */
	protected void restoreAt(int slot) {
		CombatItem item = get(slot);
		if (item == null) return;
		item.restoreUses();
	}

}
