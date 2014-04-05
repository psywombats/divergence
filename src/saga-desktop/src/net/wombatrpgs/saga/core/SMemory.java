/**
 *  SMemory.java
 *  Created on Apr 4, 2014 8:00:28 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import net.wombatrpgs.mgne.core.Memory;
import net.wombatrpgs.saga.rpg.HeroParty;

/**
 * Saga memory for storing specialized saga data in mgne.
 */
public class SMemory extends Memory {
	
	protected HeroParty party;

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#storeFields()
	 */
	@Override
	protected void storeFields() {
		super.storeFields();
		this.party = SGlobal.heroes;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#unloadFields()
	 */
	@Override
	protected void unloadFields() {
		super.unloadFields();
		SGlobal.heroes = this.party;
	}

}
