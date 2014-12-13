/**
 *  CollectableSet.java
 *  Created on Sep 22, 2014 11:41:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mgne.core.MGlobal;

/**
 * A bunch of collectables. Ensure this remains a POJO as it's serialized in
 * JSON directly.
 */
public class CollectableSet {
	
	protected Map<Collectable, Integer> quantities;
	
	/**
	 * Creates an empty collectable set.
	 */
	public CollectableSet() {
		quantities = new HashMap<Collectable, Integer>();
	}
	
	/**
	 * Adds a collectable to the collection.
	 * @param	collectable		The collectable to add
	 */
	public void addCollectable(Collectable collectable) {
		Integer quantity = quantities.get(collectable);
		if (quantity == null) quantity = 0;
		quantities.put(collectable, quantity + 1);
	}
	
	/**
	 * Adds a collectable based on its data key.
	 * @param	mdoKey			The key of the mdo of the collectable to add
	 */
	public void addCollectable(String mdoKey) {
		addCollectable(new Collectable(mdoKey));
	}
	
	/**
	 * Removes a single collectable from the collection.
	 * @param	collectable		A collectable to remove
	 */
	public void removeCollectable(Collectable collectable) {
		int quantity = quantities.get(collectable);
		if (quantity > 0) {
			quantities.put(collectable, quantity - 1);
		} else {
			MGlobal.reporter.warn("Not enough collectable: " + collectable);
		}
	}
	
	/**
	 * Checks the quantity of a collectable in the collection.
	 * @param	collectable		The collectable to check quantity of
	 * @return					The number of that collectable in stock
	 */
	public int getQuantity(Collectable collectable) {
		Integer quantity = quantities.get(collectable);
		return (quantity == null) ? 0 : quantity;
	}
	
	/**
	 * Converts the collectables map into a set of name/quantity pairings. This
	 * is usually used for display.
	 * @return					A map of name to quantity
	 */
	public Map<String, Integer> toNameQuantityPairs() {
		Map<String, Integer> pairs = new HashMap<String, Integer>();
		for (Collectable key : quantities.keySet()) {
			pairs.put(key.getName(), getQuantity(key));
		}
		return pairs;
	}
	
	/**
	 * Counts the number of unique items appearing in this collection.
	 * @return					The number of unique items contained
	 */
	public int getTypeCount() {
		return quantities.size();
	}

}
