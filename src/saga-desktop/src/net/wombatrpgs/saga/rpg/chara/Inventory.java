/**
 *  ItemListable.java
 *  Created on Apr 12, 2014 5:02:21 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.Arrays;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.CombatItem;


/**
 * Can produce a list of combat items.
 */
public abstract class Inventory {
	
	protected int capacity;
	protected CombatItem[] items;
	
	/**
	 * Produces an inventory capable of storing up to a number of items. Empty.
	 * @param	capacity		The max number of items in this inventory.
	 */
	public Inventory(int capacity) {
		this.capacity = capacity;
		items = new CombatItem[capacity];
	}
	
	/** @return A list of all held items, but it does include null! */
	public List<CombatItem> getItems() { return Arrays.asList(items); }
	
	/**
	 * Tests for reserved at a slot location. A slot is reserved if nothing is
	 * there and can't be added by the player (usually used for mutant abils).
	 * @param	slot			The slot to check
	 * @return					True if that slot is reserved
	 */
	public abstract boolean reservedAt(int slot);
	
	/**
	 * Fetches the item at the given slot, or null if no item is there.
	 * @param	slot			The slot to fetch from
	 * @return					The item there, or null
	 */
	public CombatItem get(int slot) {
		if (slot >= capacity) {
			MGlobal.reporter.warn("Out of bounds inventory check " + slot);
			return null;
		} else {
			return items[slot];
		}
	}
	
	/**
	 * Sets the contents of a slot.
	 * @param	slot			The slot to set
	 * @param	item			The item to set it with
	 * @return					The item that was there
	 */
	public CombatItem set(int slot, CombatItem item) {
		if (slot >= capacity) {
			MGlobal.reporter.warn("Out of bounds inventory check " + slot);
			return null;
		} else {
			CombatItem old = get(slot);
			items[slot] = item;
			if (item != null) {
				item.setContainer(this);
			}
			if (old != null) {
				old.setContainer(null);
			}
			return old;
		}
	}
	
	/**
	 * Returns the size of the item collection. This is the capacity, not how
	 * many items are actually there.
	 * @return					The number of available slots
	 */
	public int slotCount() {
		return capacity;
	}
	
	/**
	 * Swaps the items in two slots.
	 * @param	slot1			The slot to swap
	 * @param	slot2			The other slot to swap
	 */
	public void swap(int slot1, int slot2) {
		set(slot2, set(slot1, get(slot2)));
	}
	
	/**
	 * Destroys the item at the selected slot.
	 * @param	slot			The slot with the item to drop
	 */
	public void drop(int slot) {
		set(slot, null);
	}
	
	/**
	 * Destroys the item in this inventory.
	 * @param	item			The item held by this inventory to destroy
	 */
	public void drop(CombatItem item) {
		drop(slotFor(item));
	}
	
	/**
	 * Returns the index of the slot that the item occupies, or -1 if the item
	 * is not in this inventory. Null items are never present.
	 * @param	item			The item to check
	 * @return					The slot of that item, or -1 if not present
	 */
	public int slotFor(CombatItem item) {
		if (item == null) return -1;
		for (int i = 0; i < capacity; i += 1) {
			if (items[i] == item) {
				return i;
			}
		}
		return -1;
	}

}
