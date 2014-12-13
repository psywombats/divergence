/**
 *  SagaStats.java
 *  Created on Apr 2, 2014 10:31:24 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.stats;

import java.util.Arrays;
import java.util.Collection;

import net.wombatrpgs.mgne.rpg.StatEnumLink;
import net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable;
import net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable;
import net.wombatrpgs.sagaschema.rpg.stats.NumericStatModMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.StatEntryMDO;
import net.wombatrpgs.sagaschema.rpg.stats.StatModMDO;
import net.wombatrpgs.sagaschema.rpg.stats.StatSetMDO;

/**
 * The SaGa version of the MGN stats. Theoretically it's json-serializable.
 */
public class SagaStats extends StatEnumLink {
	
	/**
	 * Creates a stats set with all default values.
	 */
	public SagaStats() {
		
	}
	
	/**
	 * Creates a new set of stats from an existing stat set.
	 * @param	mdo				The data to read from
	 */
	public SagaStats(StatSetMDO mdo) {
		setStat(Stat.MHP,	mdo.mhp	);
		setStat(Stat.HP,	mdo.hp	);
		setStat(Stat.STR,	mdo.str	);
		setStat(Stat.DEF,	mdo.def	);
		setStat(Stat.AGI,	mdo.agi	);
		setStat(Stat.MANA,	mdo.mana);
		updateFlags(Arrays.asList(mdo.flags), true);
	}
	
	/**
	 * Creates a new set of stats from existing numeric-only list. Null works.
	 * @param	mdo					The data to read from, or null
	 */
	public SagaStats(NumericStatModMDO mdo) {
		if (mdo != null) {
			for (StatEntryMDO entryMDO : mdo.stats) {
				setStat(entryMDO.stat, entryMDO.value);
			}
		}
	}
	
	/**
	 * Creates a new set of stats from an existing stat list. Null is fine too.
	 * @param	mdo				The data to read from, or null
	 */
	public SagaStats(StatModMDO mdo) {
		if (mdo != null) {
			for (StatEntryMDO entryMDO : mdo.stats) {
				setStat(entryMDO.stat, entryMDO.value);
			}
			updateFlags(Arrays.asList(mdo.flags), true);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.rpg.StatEnumLink#numerics()
	 */
	@Override
	protected Collection<? extends NumericStatLinkable> numerics() {
		return Arrays.asList(Stat.values());
	}

	/**
	 * @see net.wombatrpgs.mgne.rpg.StatEnumLink#flags()
	 */
	@Override
	protected Collection<? extends FlagStatLinkable> flags() {
		return Arrays.asList(Flag.values());
	}

}
