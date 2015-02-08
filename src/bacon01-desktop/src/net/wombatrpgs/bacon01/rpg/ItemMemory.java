/**
 *  ItemMemory.java
 *  Created on Jan 26, 2015 10:00:42 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.rpg;

/**
 * Serialized item.
 */
public class ItemMemory {
	
	public String itemKey;
	public int quantity;
	
	public ItemMemory() {
		
	}
	
	public ItemMemory(InventoryItem item) {
		this.itemKey = item.getKey();
		this.quantity = item.getQuantity();
	}
}
