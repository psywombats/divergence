/**
 *  Party.java
 *  Created on Feb 13, 2014 12:29:09 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.tacticsschema.rpg.GameUnitMDO;
import net.wombatrpgs.tacticsschema.rpg.PartyMDO;

/**
 * Any party of dudes. Could be the hero, could be enemies. Either way, it
 * contains the units themselves and whatever shitty items or whatever they
 * picked up along the way.
 */
public class Party {
	
	protected static final String KEY_DEFAULT_PARTY = "party_default";
	
	protected List<GameUnit> units;
	
	/**
	 * Creates a new party with nobody in it. Boo hoo.
	 */
	public Party() {
		units = new ArrayList<GameUnit>();
	}
	
	/**
	 * Recruits some chump to the party!
	 * @param	unit			The chump in question
	 */
	public void addUnit(PlayerUnit unit) {
		units.add(unit);
	}
	
	/**
	 * Pushes all members of some data party into this party.
	 * @param	mdo				The data to extract from
	 */
	public void mergeParty(PartyMDO mdo) {
		for (String key : mdo.units) {
			GameUnitMDO unitMDO = MGlobal.data.getEntryFor(key, GameUnitMDO.class);
			units.add(GameUnit.createGameUnit(unitMDO));
		}
	}
	
	/**
	 * Adds heroes from the default party to this party. Used for init.
	 */
	public void addDefault() {
		mergeParty(MGlobal.data.getEntryFor(KEY_DEFAULT_PARTY, PartyMDO.class));
	}
	
	/**
	 * Recruits every dude here into a battle?
	 * @param	battle			The battle to add to
	 */
	public void addToBattle(Battle battle) {
		for (GameUnit unit : units) {
			// TODO: tactics: deploy in formation
			unit.addToBattle(battle, MGlobal.getHero().getTileX(), MGlobal.getHero().getTileY());
		}
	}

}
