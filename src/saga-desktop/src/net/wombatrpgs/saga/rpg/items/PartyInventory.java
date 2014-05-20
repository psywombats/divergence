/**
 *  PartyInventory.java
 *  Created on Apr 12, 2014 11:53:29 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import net.wombatrpgs.saga.rpg.chara.HeroParty;


/**
 * Inventory for the whole party.
 */
public class PartyInventory extends Inventory {
	
	protected static final int INVENTORY_SIZE = 10;
	
	protected HeroParty owner;
	
	/**
	 * Creates an empty party inventory.
	 * @param	party			The heroes that own this inventory
	 */
	public PartyInventory(HeroParty party) {
		super(INVENTORY_SIZE);
		this.owner = party;
	}
	
	/**
	 * Sells the item at a certain slot. Null is fine.
	 * @param	i				The index of the slot to sell at
	 */
	public void sellAt(int i) {
		CombatItem item = get(i);
		if (item == null) return;
		if (!item.isSellable()) {
			// TODO: sfx: failure sfx
			return;
		}
		owner.addGP(item.getCost(true));
		drop(i);
		// TODO: sfx: sell sfx
	}

}
