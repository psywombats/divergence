/**
 *  PlayerParty.java
 *  Created on Feb 14, 2014 12:51:17 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.tacticsschema.rpg.PartyMDO;

/**
 * Simple override to keep track of hero, basically.
 */
public class PlayerParty extends Party {
	
	protected static final String KEY_DEFAULT_PARTY = "party_default";

	protected PlayerUnit hero;
	
	/**
	 * Creates a player party from default data. Assumes the first guy in the
	 * list is 1) playable and 2) the hero, obviously.
	 */
	public PlayerParty() {
		super();
		mergeParty(MGlobal.data.getEntryFor(KEY_DEFAULT_PARTY, PartyMDO.class));
		hero = (PlayerUnit) units.get(0);	// casting aughhhh w/e
	}
	
	/** @return The protagonist's game unit */
	public PlayerUnit getHero() { return hero; }
}
