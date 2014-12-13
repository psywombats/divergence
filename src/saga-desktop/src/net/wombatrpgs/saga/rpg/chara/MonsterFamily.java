/**
 *  MonsterFamily.java
 *  Created on May 2, 2014 7:13:05 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.chara.MeatGroupMDO;
import net.wombatrpgs.sagaschema.rpg.chara.MonsterFamilyMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.TransformationMDO;

/**
 * A class of monster for transformation purposes. Uses a singleton manager.
 */
public class MonsterFamily {
	
	protected static Map<MonsterFamilyMDO, MonsterFamily> families;
	
	protected MonsterFamilyMDO mdo;
	
	/** @return The unique key of this family (its mdo key) */
	public String getKey() { return mdo.key; }
	
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
	
	/**
	 * Transforms the eating character into some other character based on the
	 * meat of some other character. Meant to be called by the eater.
	 * @param	eater			The character doing the eating
	 * @param	dropper			The character getting eaten
	 */
	public void transform(Chara eater, Chara dropper) {
		CharaMDO result = getTransformResult(eater, dropper);
		if (result != null) {
			eater.transform(result);
		}
	}
	
	/**
	 * Determines the result of one character eating another. Could return null
	 * if nothing would happen.
	 * @param	eater			The character doing the eating
	 * @param	dropper			The character being eaten
	 * @return					The MDO of the eating character after eating
	 */
	public CharaMDO getTransformResult(Chara eater, Chara dropper) {
		MonsterFamily otherFamily = dropper.getFamily();
		MeatGroupMDO meatGroup = otherFamily.getMeatGroup();
		
		// Find the outgoing link from our family with their meat group
		TransformationMDO link = null;
		for (TransformationMDO transform : mdo.transformations) {
			if (meatGroup.key.equals(transform.eat)) {
				if (link == null) {
					link = transform;
				} else {
					MGlobal.reporter.warn("2x links to group from " + mdo.key);
				}
			}
		}
		
		// Nothing happens when we eat them
		if (link == null) {
			return null;
		}
		
		// Find the best power level fit for their meat power
		int power = dropper.getEatLevel();
		if (eater.getEatLevel() > power) power = eater.getEatLevel();
		MonsterFamily resultFamily = get(link.result);
		List<CharaMDO> charas = MGlobal.data.getAll(CharaMDO.class);
		CharaMDO best = null;
		int bestPower = -1;
		for (CharaMDO charaMDO : charas) {
			if (!resultFamily.mdo.key.equals(charaMDO.family)) continue;
			if (charaMDO.meatTargetLevel > power) continue;
			if (charaMDO.meatTargetLevel > bestPower) {
				best = charaMDO;
				bestPower = charaMDO.meatTargetLevel;
			}
		}
		
		// If no monster fits, take the lowest leveled in the family
		if (best == null) {
			for (CharaMDO charaMDO : charas) {
				if (!resultFamily.mdo.key.equals(charaMDO.family)) continue;
				if (best == null || charaMDO.meatTargetLevel < bestPower) {
					best = charaMDO;
					bestPower = charaMDO.meatTargetLevel;
				}
			}
		}
		if (best == null) {
			MGlobal.reporter.err("Empty family: " + otherFamily.mdo.key);
			return null;
		}
		
		// Can't transform into monster we haven't implemented yet
		// change this once they're updated
		if (best.meatEatLevel > 3) {
			return null;
		}
		
		return best;
	}
	
	/**
	 * Internal constructor. Makes a monster family from data.
	 * @param	mdo				The data to create from
	 */
	protected MonsterFamily(MonsterFamilyMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Finds the meat group containing this class in the list of all groups.
	 * @return					The meat group for this family
	 */
	protected MeatGroupMDO getMeatGroup() {
		List<MeatGroupMDO> groups = MGlobal.data.getAll(MeatGroupMDO.class);
		MeatGroupMDO result = null;
		for (MeatGroupMDO groupMDO : groups) {
			for (String familyKey : groupMDO.families) {
				if (mdo.key.equals(familyKey)) {
					if (result == null) {
						result = groupMDO;
					} else {
						MGlobal.reporter.warn("2x meat groups: " + mdo.key);
					}
				}
			}
		}
		if (result == null) {
			MGlobal.reporter.err("No meat group found: " + mdo.key);
		}
		return result;
	}

}
