/**
 *  CharaMemory.java
 *  Created on Dec 13, 2014 3:24:21 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import net.wombatrpgs.saga.rpg.items.InventoryMemory;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.sagaschema.rpg.chara.data.Gender;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;

/**
 * The saved serialized snapshot of a character.
 */
public class CharaMemory {
	
	public SagaStats stats;
	public String animKey;
	public String graphicKey;
	public InventoryMemory inventory;
	public String statusKey;
	public String monsterFamilyKey;
	public String name;
	public Race race;
	public Gender gender;
	public String species;
	
	/**
	 * Creates a new snapshot of an rpg character.
	 * @param	chara			The chara to take snapshot of
	 */
	public CharaMemory(Chara chara) {
		this.stats = chara.getStats();
		this.animKey = chara.getAppearance().getKey();
		this.graphicKey = chara.getPortrait() == null ? null : chara.getPortrait().getKey();
		this.inventory = new InventoryMemory(chara.getInventory());
		this.statusKey = chara.getStatus() == null ? null : chara.getStatus().getKey();
		this.monsterFamilyKey = chara.getMonsterFamilyKey();
		this.name = chara.getName();
		this.race = chara.getRace();
		this.gender = chara.getGender();
		this.species = chara.getSpecies();
	}

}
