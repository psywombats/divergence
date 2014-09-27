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

import javax.swing.JOptionPane;

import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.tree.SchemaNode;
import net.wombatrpgs.mgnse.wizard.Wizard;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilityType;
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
		SchemaNode node = Global.instance().getNode(CombatItemMDO.class);
		List<CombatItemMDO> itemMDOs = new ArrayList<CombatItemMDO>();
		recursivelyAdd(node, itemMDOs);
		for (CombatItemMDO itemMDO : itemMDOs) {
			if (itemMDO.type != AbilityType.ITEM) continue;
			if (itemMDO.warhead == null) continue;
			String className = itemMDO.warhead.clazz;
			String key = itemMDO.warhead.key;
			Set<Stat> stats = EnumSet.noneOf(Stat.class);
			Integer tier = itemMDO.tier;
			if (isClass(className, EffectBoostMDO.class)) {
				EffectBoostMDO mdo = loadMDO(key, EffectBoostMDO.class);
				stats.add((mdo.powerStat == null) ? Stat.STR : mdo.powerStat);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectDefendMDO.class)) {
				stats.add(Stat.DEF);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectAttackMDO.class)) {
				EffectAttackMDO mdo = loadMDO(key, EffectAttackMDO.class);
				stats.add(mdo.attackStat);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectFixedMDO.class)) {
				EffectFixedMDO mdo = loadMDO(key, EffectFixedMDO.class);
				stats.add(mdo.accStat);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectMultihitMDO.class)) {
				EffectMultihitMDO mdo = loadMDO(key, EffectMultihitMDO.class);
				stats.add(mdo.attackStat);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectDebuffMDO.class)) {
				EffectDebuffMDO mdo = loadMDO(key, EffectDebuffMDO.class);
				stats.add(mdo.attackStat);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectStatusMDO.class)) {
				EffectStatusMDO mdo = loadMDO(key, EffectStatusMDO.class);
				if (mdo.accStat != null) stats.add(mdo.accStat);
				stats.add(Stat.MHP);
			} else if (isClass(className, EffectPassiveMDO.class)) {
				stats.add(Stat.MHP);
				//EffectPassiveMDO mdo = loadMDO(key, EffectPassiveMDO.class);
				// never mind
			}
			if ((tier == null || tier == 0) && stats.size() > 0) {
				Global.instance().warn(itemMDO.key + " has no tier");
				continue;
			}
			stats.remove(Stat.MANA);
			if (stats.size() == 0) {
				continue;
			}
			List<StatEntryMDO> entries = new ArrayList<StatEntryMDO>();
			for (Stat stat : stats) {
				StatEntryMDO entryMDO = new StatEntryMDO();
				entryMDO.stat = stat;
				entryMDO.value = (float) tier;
				entryMDO.value *= (stat == Stat.MHP) ? 9 : 2;
				entries.add(entryMDO);
			}
			itemMDO.robostats.stats = new StatEntryMDO[entries.size()];
			entries.toArray(itemMDO.robostats.stats);
			frame.getLogic().getOut().writeNewSchema(itemMDO);
		}
		
		JOptionPane.showMessageDialog(frame,
				"Robostats regenerated successfully.\n",
				"Generation Complete",
				JOptionPane.INFORMATION_MESSAGE);
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
