/**
 *  EnemyParty.java
 *  Created on Apr 23, 2014 9:10:52 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.PartyEntryMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.data.EncounterMemberMDO;

/**
 * A party controlled by the enemy AI.
 */
public class EnemyParty extends Party {
	
	protected List<Enemy> allEnemies;

	/**
	 * Superclass constructor.
	 * @param	mdo				The data to create party from
	 */
	public EnemyParty(PartyMDO mdo) {
		super(mdo);
	}
	
	/**
	 * Constructs an enemy party that fits the constraints of an encounter.
	 * @param mdo
	 */
	public EnemyParty(EncounterMDO mdo) {
		super(constructMDO(mdo));
	}
	
	/** @return All enemies in this party */
	public List<Enemy> getEnemies() { return allEnemies; }
	
	/**
	 * Calculates how much GP the player should get for beating this party by
	 * summing the death gold of its members.
	 * @return					The gold the player wins for defeating us
	 */
	public int getDeathGold() {
		int gp = 0;
		for (Chara chara : allEnemies) {
			gp += chara.getDeathGold();
		}
		return gp;
	}
	
	/**
	 * Selects a meat-dropping chara that dropped meat this battle. Sometimes
	 * returns null if no characters are meaty or meat is not abundant.
	 * @return					The chara who dropped meat, or null for none
	 */
	public Chara chooseMeatFamily() {
		int chance = SGlobal.settings.getMeatChance();
		if (MGlobal.rand.nextInt(100) > chance) {
			return null;
		}
		List<Chara> candidates = new ArrayList<Chara>();
		for (int i = 0; i < groupCount(); i += 1) {
			Chara candidate = getFront(i);
			if (candidate.getFamily() != null) {
				candidates.add(candidate);
			}
		}
		if (candidates.size() == 0) {
			return null;
		} else {
			return candidates.get(MGlobal.rand.nextInt(candidates.size()));
		}
	}
	
	/**
	 * Selects a loot item to drop for this battle. Sometimes returns null if no
	 * characters have a loot items assigned or loot is not abundant (RNG).
	 * @return					The item dropped, or null for none
	 */
	public CombatItem chooseLoot() {
		int chance = SGlobal.settings.getLootChance();
		if (MGlobal.rand.nextInt(100) > chance) {
			return null;
		}
		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < groupCount(); i += 1) {
			Chara candidate = getFront(i);
			String key = candidate.getLootKey();
			if (MapThing.mdoHasProperty(key)) {
				keys.add(key);
			}
		}
		if (keys.size() == 0) {
			return null;
		} else {
			return new CombatItem(keys.get(MGlobal.rand.nextInt(keys.size())));
		}
	}
	
	/**
	 * Generates a party mdo based on an encounter mdo.
	 * @param	encounter		The mdo to base the party off
	 * @return					The converted party mdo
	 */
	protected static PartyMDO constructMDO(EncounterMDO encounter) {
		List<PartyEntryMDO> partyMDOs = new ArrayList<PartyEntryMDO>();
		for (EncounterMemberMDO memberMDO : encounter.members) {
			String amt = memberMDO.amount;
			int min = Integer.valueOf(amt.substring(0, amt.indexOf('-')));
			int max = Integer.valueOf(amt.substring(amt.indexOf('-') + 1));
			PartyEntryMDO entryMDO = new PartyEntryMDO();
			entryMDO.monster = memberMDO.enemy;
			if (max == min) {
				entryMDO.count = max;
			} else {
				entryMDO.count = MGlobal.rand.nextInt(max - min) + min;
			}
			if (entryMDO.count > 0) {
				partyMDOs.add(entryMDO);
			}
		}
		
		PartyMDO party = new PartyMDO();
		party.description = "Generated from EncounterMDO";
		party.members = new PartyEntryMDO[partyMDOs.size()];
		partyMDOs.toArray(party.members);
		return party;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.chara.Party#instantiateChara(java.lang.String)
	 */
	@Override
	protected Chara instantiateChara(String mdoKey) {
		Enemy enemy = new Enemy(mdoKey);
		if (allEnemies == null) {
			allEnemies = new ArrayList<Enemy>();
		}
		allEnemies.add(enemy);
		return enemy;
	}

}
