/**
 *  MonsterFamily.java
 *  Created on May 2, 2014 7:13:05 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.sagaschema.rpg.chara.MonsterFamilyMDO;

/**
 * A class of monster for transformation purposes. Uses a singleton manager.
 */
public class MonsterFamily {
	
	protected static Map<MonsterFamilyMDO, MonsterFamily> families;
	
	protected MonsterFamilyMDO mdo;
	
	/**
	 * Internal constructor. Makes a monster family from data.
	 * @param	mdo				The data to create from
	 */
	protected MonsterFamily(MonsterFamilyMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Returns the monster family indicated by the MDO, either by creating it
	 * from scratch or returning the singleton copy.
	 * @param	mdo				The data to get the family for
	 * @return					The family constructed from that data
	 */
	public static MonsterFamily get(MonsterFamilyMDO mdo) {
		if (families == null) {
			families = new HashMap<MonsterFamilyMDO, MonsterFamily>();
		}
		MonsterFamily family = families.get(mdo);
		if (family == null) {
			family = new MonsterFamily(mdo);
			families.put(mdo, family);
		}
		return family;
	}
	
	/**
	 * Returns the monster family indicate by the MDO key, either by creating it
	 * from scratch or returning the singleton copy.
	 * @param	key				The key of the data to get the family for
	 * @return					The family constructed from that data
	 */
	public static MonsterFamily get(String key) {
		return get(MGlobal.data.getEntryFor(key, MonsterFamilyMDO.class));
	}

}
