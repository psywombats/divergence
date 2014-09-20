/**
 *  ItemListable.java
 *  Created on Apr 12, 2014 5:02:21 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import java.util.Arrays;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;

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
	 * Defaults to no slots reserved.
	 * @param	slot			The slot to check
	 * @return					True if that slot is reserved
	 */
	public boolean reservedAt(int slot) {
		return false;
	}
	
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
				item.onAddedTo(this);
			}
			if (old != null) {
				old.onAddedTo(null);
			}
			return old;
		}
	}
	
	/**
	 * Calculates the price of the item at the given slot. Most of the time this
	 * is the sell value, but for shops it's the buy value.
	 * @param	i				The index of the slot to check
	 * @return					The value of the item at the given slot, in GP
	 */
	public int valueAt(int i) {
		return get(i).getCost(true);
	}
	
	/**
	 * Calculates the price of an item in this inventory. Will blow up if the
	 * inventory does not actually contain the item.
	 * @param	item			The item to value check
	 * @return					The value of that item, in GP
	 */
	public final int valueOf(CombatItem item) {
		return valueAt(slotFor(item));
	}
	
	/**
	 * Returns the size of the item collection. This is the capacity, not how
	 * many items are actually there.
	 * @return					The number of available slots
	 */
	public final int slotCount() {
		return capacity;
	}
	
	/**
	 * Swaps the items in two slots.
	 * @param	slot1			The slot to swap
	 * @param	slot2			The other slot to swap
	 */
	public final void swap(int slot1, int slot2) {
		CombatItem item1 = get(slot1);
		CombatItem item2 = get(slot2);
		remove(slot1);
		remove(slot2);
		set(slot1, item2);
		set(slot2, item1);
	}
	
	/**
	 * Removes the item at the selected slot. Does not dispose it.
	 * @param	slot			The slot with the item to drop
	 */
	public final void remove(int slot) {
		set(slot, null);
	}
	
	/**
	 * Removes the item in this inventory. Does not dispose it.
	 * @param	item			The item held by this inventory to destroy
	 */
	public final void drop(CombatItem item) {
		if (item == null) return;
		remove(slotFor(item));
	}
	
	/**
	 * Returns the index of the slot that the item occupies, or -1 if the item
	 * is not in this inventory. Null items are never present.
	 * @param	item			The item to check
	 * @return					The slot of that item, or -1 if not present
	 */
	public final int slotFor(CombatItem item) {
		if (item == null) return -1;
		for (int i = 0; i < capacity; i += 1) {
			if (get(i) == item) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Checks if this inventory can hold any more items.
	 * @return					True if all slots are full
	 */
	public final boolean isFull() {
		for (int i = 0; i < capacity; i += 1) {
			if (get(i) == null && !reservedAt(i)) return false;
		}
		return true;
	}
	
	/**
	 * Attempts to add an item at the first available slot.
	 * @param	item			The item to add
	 * @return					True if it was added, false if we were full
	 */
	public final boolean add(CombatItem item) {
		for (int i = 0; i < capacity; i += 1) {
			if (get(i) == null && !reservedAt(i)) {
				set(i, item);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if this inventory contains an item of the given type. Note that
	 * this checks keys rather than specific instantiations of the item.
	 * @param	itemKey			The item type to check for
	 * @return					True if this inventory contains that type
	 */
	public final boolean containsItemType(String itemKey) {
		for (CombatItem other : items) {
			if (other != null && other.getKey().equals(itemKey)) {
				return true;
			}
		}
		return false;
	}

}
