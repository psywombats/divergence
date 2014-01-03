/**
 *  Inventory.java
 *  Created on Oct 21, 2013 12:48:05 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.rpg.GameUnit;

/**
 * A bag of infinite item holding. This represents the inventory of any game
 * unit. I forget why this isn't an array of items, but it certainly made sense
 * in the GAR engine so I'll use it here. The items list includes duplicate
 * items, ie, instead of containing HealPotionMDOx3 it contains HealPotion,
 * Healpotion, and another Healpotion. When the inventory is used for display
 * purposes, identical names get consolidated.
 */
public class Inventory {
	
	protected GameUnit parent;
	protected List<Item> items;
	
	/**
	 * Creates a new inventory for the specified game unit.
	 * @param	parent			The unit to create for
	 */
	public Inventory(GameUnit parent) {
		this.parent = parent;
		items = new ArrayList<Item>();
	}
	
	/** @return The raw item data of this inventory */
	public List<Item> getItems() { return items; }
	
	/**
	 * Throws an item into the inventory.
	 * @param	item			The item to add
	 */
	public void addItem(Item item) {
		items.add(item);
	}
	
	/**
	 * Removes an item by reference. Shouldn't ne neeeded too often?
	 * @param	item			The item to remove
	 */
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	/**
	 * Converts this inventory into a list of item names/img/amount. This
	 * should be called once every time the inventory needs to be displayed,
	 * because the order that the names are displayed in matters. Consolidates
	 * duplicate items.
	 * @return					A table matching item names to quantities
	 */
	public FlatInventory flatten() {
		return new FlatInventory(this);
	}
	
	/**
	 * Finds the first item with a matching name and returns it. Returns null if
	 * nothing matches that name.
	 * @param	name			The name of the item to fetch
	 * @return					The first occurrence of an item with that name
	 */
	public Item getNamedItem(String name) {
		for (Item item : items) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

}
