/**
 *  MutantAbilityList.java
 *  Created on May 24, 2014 8:10:00 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.mutant;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.abil.MutationSettingsMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.MutantAbilMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * In charge of assigning random abilities to mutants.
 */
public class MutationSettings {
	
	public MutationSettingsMDO mdo;
	
	/**
	 * Creates a new mutant ability list from data.
	 * @param	mdo				The data to create from
	 */
	public MutationSettings(MutationSettingsMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Generates a mutant ability based on the meat power level of a monster.
	 * Uses the RNG to select an ability and discard it based on the rarity of
	 * that ability. Will probably hang if no abilities in the list...
	 * @param	level			The meat level to generate for
	 * @return					An appropriate ability to learn
	 */
	public CombatItem generateAbil(int level) {
		// we'll break out by returning the correct abil
		while (true) {
			int index = MGlobal.rand.nextInt(mdo.abils.length);
			MutantAbilMDO abilMDO = mdo.abils[index];
			int roll = MGlobal.rand.nextInt(100);
			if (abilMDO.level > level) {
				roll /= 2;
			}
			if (roll < abilMDO.chance) {
				return new CombatItem(abilMDO.abil);
			}
		}
	}
	
	/**
	 * Produces a random level up option (stats vs ability).
	 * @param	chara			The character to grant the gain to
	 * @param	level			The level of the slain enemies
	 * @return					A random level up
	 */
	public Mutation randomOption(Chara chara, int level) {
		List<MutantEvent> spread = new ArrayList<MutantEvent>();
		for (int i = 0; i < mdo.weightHp; i += 1) {
			spread.add(MutantEvent.DAMAGED);
		}
		for (int i = 0; i < mdo.weightDef; i += 1) {
			spread.add(MutantEvent.DAMAGED_PHYSICALLY);
		}
		for (int i = 0; i < mdo.weightAbil; i += 1) {
			spread.add(MutantEvent.USED_ABILITY);
		}
		for (int i = 0; i < mdo.weightAgi; i += 1) {
			spread.add(MutantEvent.USED_AGI);
		}
		for (int i = 0; i < mdo.weightStr; i += 1) {
			spread.add(MutantEvent.USED_STR);
		}
		for (int i = 0; i < mdo.weightMana; i += 1) {
			spread.add(MutantEvent.USED_MANA);
		}
		switch (spread.get(MGlobal.rand.nextInt(spread.size()))) {
		case DAMAGED:				return genHealthOption(chara);
		case DAMAGED_PHYSICALLY:	return new MutationStat(chara, Stat.DEF);
		case USED_ABILITY:			return new MutationAbil(chara, level);
		case USED_AGI:				return new MutationStat(chara, Stat.AGI);
		case USED_MANA:				return new MutationStat(chara, Stat.MANA);
		case USED_STR:				return new MutationStat(chara, Stat.STR);
		default:					return null;
		}
	}
	
	/**
	 * Generates an MHP level up.
	 * @param	chara			The chara gaining the level
	 * @return					A mutant level up with appropriate values
	 */
	public Mutation genHealthOption(Chara chara) {
		int gain = MGlobal.rand.nextInt(mdo.maxHealth - mdo.minHealth);
		gain += mdo.minHealth;
		return new MutationStat(chara, Stat.MHP, gain);
	}
	
	/**
	 * Checks if a mutant should actually receive a level up this battle by
	 * checking against the level chance.
	 * @return					True if a mutation should be awarded
	 */
	public boolean shouldMutate() {
		return MGlobal.rand.nextInt(100) <= mdo.mutationChance;
	}

}
