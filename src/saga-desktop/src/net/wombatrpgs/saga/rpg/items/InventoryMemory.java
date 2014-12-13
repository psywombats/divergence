/**
 *  InventoryMemory.java
 *  Created on Dec 13, 2014 3:01:12 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

/**
 * Serialized snapshot of the party's inventory.
 */
public class InventoryMemory {
	
	public ItemMemory[] items;
	
	/** Serializable constructor */
	public InventoryMemory() { }
	
	/**
	 * Creates a new snapshot from the inventory provided.
	 * @param	inventory		The inventory to memorize
	 */
	public InventoryMemory(Inventory inventory) {
		items = new ItemMemory[inventory.slotCount()];
		for (int i = 0; i < items.length; i += 1) {
			CombatItem item = inventory.get(i);
			if (item != null) {
				items[i] = new ItemMemory(item);
			}
		}
	}
	
	/**
	 * Gets the stored combat item at the nth slot. 
	 * @param	n				The index of the slot to get
	 * @return					The stored item in that slot, or null if none
	 */
	public CombatItem at(int n) {
		ItemMemory item = items[n];
		if (item == null) {
			return null;
		} else {
			return new CombatItem(item);
		}
	}

}
