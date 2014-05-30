/**
 *  MutantLevelAbil.java
 *  Created on May 27, 2014 7:40:26 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.mutant;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * A level up where a mutant gains a new ability.
 */
public class MutationAbil extends Mutation {
	
	public int lose;
	public CombatItem gain;
	
	/**
	 * Creates a level up option for a mutant an ability and gaining another.
	 * @param	chara			The character gaining/losing the abil
	 * @param	loseSlot		The slot of the ability to lose
	 * @param	gainAbil		The ability to gain
	 */
	public MutationAbil(Chara chara, int loseSlot, CombatItem gainAbil) {
		super(chara);
		this.lose = loseSlot;
		this.gain = gainAbil;
	}
	
	/**
	 * Creates a level up option for a mutant given the level of the monster
	 * they fought, randomly choosing an ability to gain and slot to lose.
	 * @param	chara			The character gaining/losing the abil
	 * @param	level
	 */
	public MutationAbil(Chara chara, int level) {
		this(chara,
				MGlobal.rand.nextInt(4),
				SGlobal.settings.getMutations().generateAbil(level));
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.mutant.Mutation#getDesc()
	 */
	@Override
	public String getDesc() {
		CombatItem loseAbil = chara.getInventory().get(lose);
		if (loseAbil == null) {
			return "acquire " + gain.getName();
		} else {
			return "lose " + loseAbil.getName() + ", acquire " + gain.getName();
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.mutant.Mutation#apply()
	 */
	@Override
	public void apply() {
		chara.getInventory().set(lose, gain);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.mutant.Mutation#getStat()
	 */
	@Override
	public Stat getStat() {
		return null;
	}

}
