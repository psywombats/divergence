/**
 *  SagaStats.java
 *  Created on Apr 2, 2014 10:31:24 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.Arrays;

import net.wombatrpgs.mgne.rpg.StatEnumLink;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.StatEntryMDO;
import net.wombatrpgs.sagaschema.rpg.stats.StatModMDO;
import net.wombatrpgs.sagaschema.rpg.stats.StatSetMDO;

/**
 * The SaGa version of the MGN stats.
 */
public class SagaStats extends StatEnumLink {
	
	/**
	 * Creates a new set of stats from an existing stat set.
	 * @param	mdo				The data to read from
	 */
	public SagaStats(StatSetMDO mdo) {
		this();
		setStat(Stat.MHP,	mdo.mhp	);
		setStat(Stat.HP,	mdo.hp	);
		setStat(Stat.STR,	mdo.str	);
		setStat(Stat.DEF,	mdo.def	);
		setStat(Stat.AGI,	mdo.agi	);
		setStat(Stat.MANA,	mdo.mana);
		updateFlags(Arrays.asList(mdo.flags), true);
	}
	
	/**
	 * Creates a new set of stats from an existing stat list.
	 * @param	mdo				The data to read from
	 */
	public SagaStats(StatModMDO mdo) {
		this();
		for (StatEntryMDO entryMDO : mdo.stats) {
			setStat(entryMDO.stat, entryMDO.value);
		}
		updateFlags(Arrays.asList(mdo.flags), true);
	}
	
	/**
	 * Internal constructor.
	 */
	protected SagaStats() {
		super(Arrays.asList(Stat.values()), Arrays.asList(Flag.values()));
	}

}
