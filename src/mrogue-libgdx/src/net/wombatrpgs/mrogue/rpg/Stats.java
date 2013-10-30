/**
 *  Stats.java
 *  Created on Aug 19, 2013 5:15:10 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;

/**
 * A collection of RPG stats associated with a character, item, etc. The idea is
 * that it can be added to another stats object in case of a boost, maintained
 * by a character to keep track of current stats, etc. Etc.
 */
public class Stats {
	
	protected StatsMDO mdo;
	
	// there used to be a zillion getters/setters here, now there aren't
	// ...so use responsibly
	public int mhp, hp;
	public int mmp, mp;
	public int speed;
	public int walkEP, attackEP;
	public int vision;
	public int defense;
	public int dmgBase, dmgRange, armor;
	public int magBase, magRange, magArmor;
	
	/**
	 * Creates a new stats object from data.
	 * @param	stats			The stats data to create from
	 */
	public Stats(StatsMDO stats) {
		this.mdo = stats;
		hp			= stats.hp;
		mhp			= stats.mhp;
		mp			= stats.mp;
		mmp			= stats.mmp;
		speed		= stats.speed;
		vision		= stats.vision;
		armor		= stats.armor;
		defense		= stats.dodge;
		dmgBase		= stats.dmgBase;
		dmgRange	= stats.dmgRange;
		magBase		= stats.magBase;
		magRange	= stats.magRange;
		magArmor	= stats.magArmor;
		walkEP		= stats.walkCost;
		attackEP	= stats.attackCost;
	}
	
	/** @return The likeliness to dodge, a percent 0-1 */
	public float getDodgeChance() { return (float) defense / 100f; }
	
	/** @return The speed modifier, as a float% */
	public float getSpeedMod() { return 100f / (float) mdo.speed; }
	
	/** 
	 * Calculates some damage via base, range, and the RNG.
	 * @return					The damage to be dealt.
	 */
	public int getDamage() {
		return dmgBase + MGlobal.rand.nextInt(dmgRange+1);
	}
	
	/** 
	 * Calculates some magic damage via base, range, and the RNG.
	 * @return					The damage to be dealt.
	 */
	public int getMagDamage() {
		return magBase + MGlobal.rand.nextInt(magRange+1);
	}
	
	/**
	 * Inflicts a set amount of HP damage. Just subtraction here.
	 * @param	damage			The amount of damage inflicted
	 */
	public void takeRawDamage(int damage) {
		hp -= damage;
		if (hp < 0) hp = 0;
		if (hp > mhp) hp = mhp;
	}
	
	/**
	 * Inflicts an amount of damage based on incoming damage and this unit's
	 * defenses.
	 * @param	damage			The physical damage to be dealt
	 * @return					The amount of raw damage inflicted
	 */
	public int takePhysicalDamage(int damage) {
		int toDeal = damage - armor;
		if (toDeal < 0) {
			return 0;
		} else {
			takeRawDamage(toDeal);
			return toDeal;
		}
	}
	
	/**
	 * Inflicts an amount of damage based on incoming damage and this unit's
	 * defenses.
	 * @param	damage			The magical damage to be dealt
	 * @return					The amount of raw damage inflicted
	 */
	public int takeMagicDamage(int damage) {
		int toDeal = damage - magArmor;
		if (toDeal < 0) {
			return 0;
		} else {
			takeRawDamage(toDeal);
			return toDeal;
		}
	}

}
