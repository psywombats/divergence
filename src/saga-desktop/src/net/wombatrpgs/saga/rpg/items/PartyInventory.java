/**
 *  PartyInventory.java
 *  Created on Apr 12, 2014 11:53:29 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SConstants;
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
	 * Creates a new inventory from serialized save data.
	 * @param	party			The party to create from
	 * @param	memory			The serialized data to load from
	 */
	public PartyInventory(HeroParty party, InventoryMemory memory) {
		super(memory);
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
			MGlobal.audio.playSFX(SConstants.SFX_FAIL);
			return;
		}
		owner.addGP(item.getCost(true));
		remove(i);
		MGlobal.audio.playSFX(SConstants.SFX_GET);
	}

}
