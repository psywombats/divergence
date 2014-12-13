/**
 *  Stats.java
 *  Created on Feb 27, 2014 6:26:00 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.rpg.data.FlagStat;
import net.wombatrpgs.mgneschema.rpg.data.FlagStatValue;
import net.wombatrpgs.mgneschema.rpg.data.NumericStat;

/**
 * RPG statistics helper class. Games should subclass this with their own
 * version that includes specifics and potentially advanced getters. Sort of
 * based on how te4 handles things.
 */
public abstract class Stats {
	
	public Map<String, Float> statValues;
	public Map<String, FlagStatValue> statTypes;
	
	/**
	 * Creates a new stats object with all defaults.
	 */
	public Stats() {
		statValues = new HashMap<String, Float>();
		statTypes = new HashMap<String, FlagStatValue>();
		for (NumericStat stat : statTypes().values()) {
			statValues.put(stat.getID(), stat.getZero());
		}
		for (FlagStat flag : flagTypes().values()) {
			statTypes.put(flag.getID(), flag.getZero());
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		for (NumericStat stat : statTypes().values()) {
			float value = statValues.get(stat.getID());
			if (value != stat.getZero()) {
				result += stat.getID() + ":" + Math.round(value) + ", ";
			}
		}
		for (FlagStat flag : flagTypes().values()) {
			boolean value = statTypes.get(flag.getID()).on();
			if (value) {
				result += flag.getID() + ":" + value + ", ";
			}
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
		} else {
			result = "[]";
		}
		return result;
	}

	/**
	 * Retrieves the value of a stat based on the ID string. The id is meant to
	 * be something like 'str' and then this will return the strength value. If
	 * no value for that stat is recorded, returns the identity for that stat,
	 * and if no stat with that ID exists, reports an error and returns null.
	 * @param	id				The unique identifier of the stat to look up
	 * @return					The value of that stat
	 */
	public Float stat(String id) {
		Float value = statValues.get(id);
		if (value != null) {
			return value;
		} else {
			NumericStat stat = statTypes().get(id);
			if (stat == null) {
				MGlobal.reporter.warn("No stat for id: " + id + " on " + this);
				return null;
			} else {
				return stat.getZero();
			}
		}
	}
	
	/**
	 * Retrieves the value of a stat based on the ID string. The id should be
	 * the name of a flag stat. If no value for that stat is recorded, defaults
	 * to off.
	 * @param	id				The unique identifier of the flag to look up
	 * @return					True if that flag is set, false otherwise
	 */
	public Boolean flag(String id) {
		FlagStatValue value = statTypes.get(id);
		if (value != null) {
			return value.on();
		} else {
			return false;
		}
	}
	
	/**
	 * Sets a stat's value by ID. Does nothing if stat does not exist.
	 * @param	id				The ID of the stat to update
	 * @param	value			The new value for the stat
	 */
	public void setStat(String id, float value) {
		statValues.put(id, value);
	}
	
	/**
	 * Combines some value to this set's stat value by ID. Does nothing if stat
	 * does not exist. This is usually addition.
	 * @param	id				The ID of the stat to update
	 * @param	value			The value to add to the stat
	 */
	public void addStat(String id, float value) {
		NumericStat stat = statTypes().get(id);
		float current = statValues.get(id);
		setStat(id, stat.combine(current, value));
	}
	
	/**
	 * Decombines some value to this set's stat value by ID. Does nothing if
	 * stat does not exist. This is usually subtraction.
	 * @param	id				The ID of the stat to update
	 * @param	value			The value to add to the stat
	 */
	public void subtractStat(String id, float value) {
		NumericStat stat = statTypes().get(id);
		float current = statValues.get(id);
		setStat(id, stat.decombine(current, value));
	}
	
	/**
	 * Updates a flag count in this stat set. This is the safe and correct way
	 * to set things like flag for equipment. Not useful for completely removing
	 * a flag etc.
	 * @param	id				The ID of the flag to update
	 * @param	value			True to add the flag, false to remove it
	 */
	public void updateFlag(String id, boolean value) {
		FlagStatValue flag = statTypes.get(id);
		if (flag == null) {
			flag = new FlagStatValue(value ? 1 : 0);
		} else {
			if (value) {
				flag = new FlagStatValue(flag.count + 1);
			} else {
				flag = new FlagStatValue(flag.count - 1);
			}
		}
		statTypes.put(id, flag);
	}
	
	/**
	 * Combines this set of stats with some other stats, like when a player
	 * equips an item with the other stats.
	 * @param	other			The other stats to merge in
	 */
	public void combine(Stats other) {
		for (NumericStat type : statTypes().values()) {
			Float value = statValues.get(type.getID());
			String id = type.getID();
			statValues.put(id, type.combine(value, other.stat(id)));
		}
		for (FlagStat type :flagTypes().values()) {
			String id = type.getID();
			FlagStatValue value = statTypes.get(id);
			FlagStatValue otherValue = other.statTypes.get(id);
			if (otherValue != null && otherValue.on()) {
				value = new FlagStatValue(value.count + 1);
				statTypes.put(id, value);
			}
		}
	}
	
	/**
	 * Decombines this set of stats with some other stats, like when a player
	 * unequips an item with the other stats.
	 * @param	other			The other stats to merge in
	 */
	public void decombine(Stats other) {
		for (NumericStat type : statTypes().values()) {
			Float value = statValues.get(type.getID());
			String id = type.getID();
			if (value == null) {
				MGlobal.reporter.err("Decombined a non-combined stat set");
			} else {
				statValues.put(id, type.decombine(value, other.stat(id)));
			}
		}
		for (FlagStat type : flagTypes().values()) {
			String id = type.getID();
			FlagStatValue value = statTypes.get(id);
			FlagStatValue otherValue = other.statTypes.get(id);
			if (value == null) {
				MGlobal.reporter.err("Decombined a non-combined stat set");
			} else if (otherValue.on()) {
				value = new FlagStatValue(value.count - 1);
				statTypes.put(id, value);
			}
		}
	}
	
	/** @return The whole list of numeric stats in the game */
	protected abstract Map<String, NumericStat> statTypes();
	
	/** @return The whole list of flag stats in the game */
	protected abstract Map<String, FlagStat> flagTypes();

}
