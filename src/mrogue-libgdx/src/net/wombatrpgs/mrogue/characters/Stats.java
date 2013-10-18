/**
 *  Stats.java
 *  Created on Aug 19, 2013 5:15:10 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;

/**
 * A collection of RPG stats associated with a character, item, etc. The idea is
 * that it can be added to another stats object in case of a boost, maintained
 * by a character to keep track of current stats, etc. Etc.
 */
public class Stats {
	
	private StatsMDO mdo;
	private int mhp, hp;
	private int mmp, mp;
	private int speed;
	private int vision;
	private int armor, defense;
	private int dmgBase, dmgRange;
	
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
	}
	
	/** @return Max health points */
	public int getMHP() { return mhp; }
	
	/** @param mhp The new max health value */
	public void setMHP(int mhp) { this.mhp = mhp; }
	
	/** @param mhp The amount to increase max health by */
	public void addMHP(int mhp) { this.mhp += mhp; }
	
	/** @return Current health points */
	public int getHP() { return hp; }
	
	/** @param newHP This stats new special value */
	public void setHP(int newHP) { this.hp = newHP; }
	
	/** @param hp The amount of special to add or deduct */
	public void addHP(int hp) { this.hp += hp; }
	
	/** @return Max special points */
	public int getMMP() { return mmp; }
	
	/** @param mmp The new max special value */
	public void setMMP(int mmp) { this.mmp = mmp; }
	
	/** @param mmp The amount to increase special points by */
	public void addMMP(int mmp) { this.mmp += mmp; }
	
	/** @return Current special points */
	public int getMP() { return mp; }
	
	/** @param newMP This stats new health value */
	public void setMP(int newMP) { this.mp = newMP; }
	
	/** @param mp The amount of health to add or deduct */
	public void addMP(int mp) { this.mp += mp; }
	
	/** @return The absolute speed of this character */
	public int getSpeed() { return speed; }
	
	/** @param speed The amount of absolute speed to add */
	public void addSpeed(int speed) { this.speed= speed; }
	
	/** @return The speed modifier, as a float% */
	public float getSpeedMod() { return 100f / (float) mdo.speed; }
	
	/** @return The vision radius, in tiles */
	public int getVision() { return vision; }
	
	/** @param vision The amount to increase vision by, in tiles */
	public void addVision(int vision) { this.vision += vision; }
	
	/** @return The armor rating, in HP */
	public int getArmor() { return this.armor; }
	
	/** @param Armor The amount of armor to add, in HP */
	public void addArmor(int armor) { this.armor += armor; }
	
	/** @return The dodge, 0-100 */
	public int getDefense() { return this.defense; }
	
	/** @return The likeliness to dodge, a percent 0-1 */
	public float getDodgeChance() { return (float) defense / 100f; }
	
	/** @param def The amount to increase def by, 0-100 chance */
	public void addDefense(int def) { this.defense += def; }
	
	/** @return The minimum melee damage, in HP */
	public int getDmgBase() { return this.dmgBase; }
	
	/** @param dmg The amount to increase min damage, in HP */
	public void addBaseDmg(int dmg) { this.dmgBase += dmg; }
	
	/** @return The damage variance, in HP */
	public int getDmgRange() { return this.dmgRange; }
	
	/** @param dmg The amount of damage potential to add, in HP */
	public void addDmgRange(int dmg) { this.dmgRange += dmg; }
	
	/** 
	 * Calculates some damage via base, range, and the RNG.
	 * @return					The damage to be dealt.
	 */
	public int getDamage() {
		return dmgBase + MGlobal.rand.nextInt(dmgRange+1);
	}
	
	/**
	 * Inflicts a set amount of HP damage. Just subtraction here.
	 * @param	damage			The amount of damage inflicted
	 */
	public void takeRawDamage(int damage) {
		hp -= damage;
		if (hp < 0) hp = 0;
	}
	
	/**
	 * Inflicts an amount of damage based on incoming damage and this unit's
	 * defenses.
	 * @param	damage			The physical damage to be dealt
	 * @return					The amount of raw damage inflicted
	 */
	public int takePhysicalDamage(int damage) {
		int toDeal = damage - getArmor();
		if (toDeal < 0) {
			return 0;
		} else {
			takeRawDamage(toDeal);
			return toDeal;
		}
	}

}
