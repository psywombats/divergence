package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogueschema.characters.data.StatsMDO;

/**
 * A version of stats, but this one's meant to be treated as something that
 * can be added or removed from a real stat set. It has the same fields and
 * the same MDO pretty much, but it can't be queried for things like speed%
 * that wouldn't make sense. This is to simplify stat calculation, or at least
 * make it less error-prone.
 */
public class StatsModifier {
	
	protected StatsMDO mdo;
	
	/**
	 * Creates a new modifier from data.
	 * @param	mdo				The data to use to create
	 */
	public StatsModifier(StatsMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Modifies the given stats object by the modifiers indicated by this
	 * modifier object. Make sure to unapply when whatever modifies the stats
	 * is removed, if necessary. 
	 * @param stats
	 */
	public void applyTo(Stats stats) {
		apply(stats, 1);
	}
	
	/**
	 * Modifies the given stats object by the inverse of what is indicated by
	 * this modifier object. Useful for removing items.
	 * @param stats
	 */
	public void unapplyTo(Stats stats) {
		apply(stats, -1);
	}
	
	/**
	 * Modifies the given stats object.
	 * @param	stats			The stats to mutate
	 * @param	sign			The sign of the operation: 1 for add, -1 for rem
	 */
	protected void apply(Stats stats, int sign) {
		stats.addHP(sign * mdo.hp);
		stats.addMHP(sign * mdo.mhp);
		stats.addMP(sign * mdo.mp);
		stats.addMMP(sign * mdo.mmp);
		stats.addSpeed(sign * mdo.speed);
		stats.addVision(sign * mdo.vision);
		stats.addBaseDmg(sign * mdo.dmgBase);
		stats.addDmgRange(sign * mdo.dmgRange);
		stats.addDefense(sign * mdo.dodge);
		stats.addArmor(sign * mdo.armor);
	}

}
