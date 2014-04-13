/**
 *  PartyInventory.java
 *  Created on Apr 12, 2014 11:53:29 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

/**
 * Inventory for the whole party.
 */
public class PartyInventory extends Inventory {
	
	protected static final int INVENTORY_SIZE = 10;
	
	/**
	 * Creates an empty party inventory.
	 */
	public PartyInventory() {
		super(INVENTORY_SIZE);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.Inventory#reservedAt(int)
	 */
	@Override
	public boolean reservedAt(int slot) {
		return false;
	}
	
	/**
	 * Checks if this inventory can hold any more items.
	 * @return					True if all slots are full
	 */
	public boolean isFull() {
		for (int i = 0; i < INVENTORY_SIZE; i += 1) {
			if (items[i] == null) return false;
		}
		return true;
	}

}
