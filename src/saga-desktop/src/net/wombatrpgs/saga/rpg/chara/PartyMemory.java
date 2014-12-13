/**
 *  PartyMemory.java
 *  Created on Dec 13, 2014 2:52:41 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import net.wombatrpgs.saga.rpg.items.CollectableSet;
import net.wombatrpgs.saga.rpg.items.InventoryMemory;

/**
 * Serializable snapshot of the player party.
 */
public class PartyMemory {
	
	public int gp;
	public String location;
	public InventoryMemory inventory;
	public CollectableSet collectables;
	public CharaMemory[] charas;
	public int[] charaOrderIndices;
	
	/** Serializable constructor */
	public PartyMemory() { }
	
	/**
	 * Creates a new snapshot of the provided party.
	 * @param	party			The party to take a snapshot of
	 */
	public PartyMemory(HeroParty party) {
		this.gp = party.getGP();
		this.location = party.getLocation();
		this.inventory = new InventoryMemory(party.getInventory());
		this.collectables = party.getCollection();
		this.charas = new CharaMemory[party.size()];
		this.charaOrderIndices = new int[party.size()];
		for (int i = 0; i < charas.length; i += 1) {
			charas[i] = new CharaMemory(party.getStoryHero(i));
			charaOrderIndices[i] = party.index(party.getStoryHero(i));
		}
	}

}
