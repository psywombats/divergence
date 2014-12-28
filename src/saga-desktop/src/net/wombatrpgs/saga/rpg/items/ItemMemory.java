/**
 *  ItemMemory.java
 *  Created on Dec 13, 2014 3:10:01 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

/**
 * The serialized snapshot of a combat item.
 */
public  class ItemMemory {
	
	public String key;
	public int uses;
	public int usesWhenAdded;
	
	/** Serializable constructor */
	public ItemMemory() { }
	
	/**
	 * Creates a new memory of a combat item.
	 * @param	item			The item to take a snapshot of
	 */
	public ItemMemory(CombatItem item) {
		this.key = item.getKey();
		this.uses = item.getUses();
		this.usesWhenAdded = item.getAddedUses();
	}
}
