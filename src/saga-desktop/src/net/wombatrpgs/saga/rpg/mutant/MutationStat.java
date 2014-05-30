/**
 *  MutantLevelStat.java
 *  Created on May 27, 2014 7:26:21 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.mutant;

import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * An option for a mutant to gain a stat on level up.
 */
public class MutationStat extends Mutation {
	
	protected Stat stat;
	protected int delta;

	/**
	 * Creates a gain option granting a character an amount of one stat.
	 * @param	chara			The character to make the gain for
	 * @param	stat			The stat to raise
	 * @param	delta			The amount to raise it by
	 */
	public MutationStat(Chara chara, Stat stat, int delta) {
		super(chara);
		this.stat = stat;
		this.delta = delta;
	}
	
	/**
	 * Creates a gain option granting a character +2 to a stat.
	 * @param	chara			The character to make the gain for
	 * @param	stat			The stat to raise
	 */
	public MutationStat(Chara chara, Stat stat) {
		this(chara, stat, 2);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.mutant.Mutation#getDesc()
	 */
	@Override
	public String getDesc() {
		return "raise " + stat.getLabel().substring(0, stat.getLabel().length() - 1);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.mutant.Mutation#apply()
	 */
	@Override
	public void apply() {
		chara.modifyStat(stat, delta);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.mutant.Mutation#getStat()
	 */
	@Override
	public Stat getStat() {
		return stat;
	}

}
