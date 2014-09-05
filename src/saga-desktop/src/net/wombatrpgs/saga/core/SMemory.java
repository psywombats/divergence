/**
 *  SMemory.java
 *  Created on Apr 4, 2014 8:00:28 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.Memory;
import net.wombatrpgs.saga.rpg.chara.HeroParty;

/**
 * Saga memory for storing specialized saga data in mgne.
 */
public class SMemory extends Memory {
	
	// fields to store/unload
	protected HeroParty party;
	protected int saveSlot;

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#storeFields()
	 */
	@Override
	protected void storeFields() {
		super.storeFields();
		this.party = SGlobal.heroes;
		this.saveSlot = SGlobal.saveSlot;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#unloadFields()
	 */
	@Override
	protected void unloadFields() {
		// fifo, bitch
		SGlobal.heroes = this.party;
		SGlobal.saveSlot = this.saveSlot;
		super.unloadFields();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#loadAssets()
	 */
	@Override
	protected void loadAssets() {
		MGlobal.assets.loadAsset(SGlobal.heroes, "loaded heroes");
		super.loadAssets();
	}

}
