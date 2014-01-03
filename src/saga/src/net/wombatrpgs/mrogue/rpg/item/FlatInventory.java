/**
 *  FlatInventory.java
 *  Created on Oct 21, 2013 2:45:11 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import java.util.ArrayList;
import java.util.List;


/**
 * An inventory data structure that's been packed into a form more suitable for
 * ui display.
 */
public class FlatInventory {
	
	List<FlatItem> flats;
	
	/**
	 * Creates a new flat inventory by taking a snapshot of a parent inventory.
	 * @param	inv				The inventory to take a snapshot of
	 */
	public FlatInventory(Inventory inv) {
		flats = new ArrayList<FlatItem>();
		for (Item item : inv.getItems()) {
			boolean inserted = false;
			for (FlatItem flat : flats) {
				if (flat.item.getName().equals(item.getName())) {
					flat.amt += 1;
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				FlatItem flat = new FlatItem(item, 1);
				flats.add(flat);
			}
		}
	}
	
	/** @return The size of the flat item list */
	public int size() { return flats.size(); }
	
	/**
	 * Fetches the flat item at the specified index. If the supplied index is
	 * out of bounds, returns null.
	 * @param	index			The index of the flat item to get
	 * @return					The flat item there, or null if none
	 */
	public FlatItem getFlat(int index) {
		if (index >= flats.size()) return null;
		return flats.get(index);
	}
	
	/**
	 * Creates a flat item, containing a number of item in inventory as well as
	 * a sample of that item.
	 */
	public class FlatItem {
		public Item item;
		public int amt;
		public FlatItem(Item item, int amt) {
			this.item = item;
			this.amt = amt;
		}
	}

}
