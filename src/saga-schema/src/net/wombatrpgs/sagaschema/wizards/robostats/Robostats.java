/**
 *  Robostats.java
 *  Created on Sep 26, 2014 8:34:15 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.wizards.robostats;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.tree.SchemaNode;
import net.wombatrpgs.mgnse.wizard.Wizard;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilityType;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;
import net.wombatrpgs.sagaschema.rpg.stats.StatEntryMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectAttackMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectBoostMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectDebuffMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectDefendMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectFixedMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectMultihitMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectPassiveMDO;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectStatusMDO;

/**
 * Regenerates robostats on items.
 */
public class Robostats extends Wizard {

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#getName()
	 */
	@Override
	public String getName() {
		return "Regenerate Item Robostats";
	}

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#run()
	 */
	@Override
	public void run() {
		SchemaNode node = Global.instance().getNode(CharaMDO.class);
		List<CombatItemMDO> itemMDOs = new ArrayList<CombatItemMDO>();
		recursivelyAdd(node, itemMDOs);
		for (CombatItemMDO itemMDO : itemMDOs) {
			if (itemMDO.type != AbilityType.ITEM) continue;
			String className = itemMDO.warhead.clazz;
			String key = itemMDO.warhead.key;
			Set<Stat> stats = EnumSet.of(Stat.MHP);
			if (isClass(className, EffectBoostMDO.class)) {
				EffectBoostMDO mdo = loadMDO(key, EffectBoostMDO.class);
				stats.add((mdo.powerStat == null) ? Stat.STR : mdo.powerStat);
			} else if (isClass(className, EffectDefendMDO.class)) {
				stats.add(Stat.DEF);
			} else if (isClass(className, EffectAttackMDO.class)) {
				EffectAttackMDO mdo = loadMDO(key, EffectAttackMDO.class);
				stats.add(mdo.attackStat);
			} else if (isClass(className, EffectFixedMDO.class)) {
				EffectFixedMDO mdo = loadMDO(key, EffectFixedMDO.class);
				stats.add(mdo.accStat);
			} else if (isClass(className, EffectMultihitMDO.class)) {
				EffectMultihitMDO mdo = loadMDO(key, EffectMultihitMDO.class);
				stats.add(mdo.attackStat);
			} else if (isClass(className, EffectDebuffMDO.class)) {
				EffectDebuffMDO mdo = loadMDO(key, EffectDebuffMDO.class);
				stats.add(mdo.attackStat);
			} else if (isClass(className, EffectStatusMDO.class)) {
				EffectStatusMDO mdo = loadMDO(key, EffectStatusMDO.class);
				stats.add(mdo.accStat);
			} else if (isClass(className, EffectPassiveMDO.class)) {
				//EffectPassiveMDO mdo = loadMDO(key, EffectPassiveMDO.class);
				// never mind
			}
			List<StatEntryMDO> entries = new ArrayList<StatEntryMDO>();
			for (Stat stat : stats) {
				if (stat == Stat.MANA) continue;
				StatEntryMDO entryMDO = new StatEntryMDO();
				entryMDO.stat = stat;
				entryMDO.value = (float) itemMDO.tier;
				entryMDO.value *= (stat == Stat.MHP) ? 9 : 2;
				entries.add(entryMDO);
			}
			itemMDO.robostats.stats = new StatEntryMDO[0];
			entries.toArray(itemMDO.robostats.stats);
			frame.getLogic().getOut().writeNewSchema(itemMDO);
		}
	}
	
	private void recursivelyAdd(SchemaNode node, List<CombatItemMDO> current) {
		if (node.isLeaf()) {
			CombatItemMDO mdo = (CombatItemMDO) frame.getLogic().getIn().instantiateData(
					CombatItemMDO.class,
					node.getFile());
			current.add(mdo);
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				recursivelyAdd((SchemaNode) node.getChildAt(i), current);
			}
		}
	}
	
	private boolean isClass(String className, Class<? extends AbilEffectMDO> clazz) {
		try {
			return Class.forName(className).isAssignableFrom(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends AbilEffectMDO> T loadMDO(String key, Class<T> clazz) {
		File file = new File(frame.getLogic().pathForSchema(clazz, key));
		return (T) frame.getLogic().getIn().instantiateData(clazz, file);
	}

}
