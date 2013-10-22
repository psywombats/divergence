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
	 * @param	stats			The stats to apply to
	 */
	public void applyTo(Stats stats) {
		apply(stats, 1);
	}
	
	/**
	 * Modifies the given stats object by the modifiers indicated by this
	 * modifier object. Make sure to unapply when whatever modifies the stats
	 * is removed, if necessary. 
	 * @param	stats			The stats to apply to
	 * @param	r				The proportion to apply in
	 */
	public void applyTo(Stats stats, float r) {
		apply(stats, r);
	}
	
	/**
	 * Modifies the given stats object by the inverse of what is indicated by
	 * this modifier object. Useful for removing items.
	 * @param	stats			The stats to unapply to
	 */
	public void unapplyTo(Stats stats) {
		apply(stats, -1);
	}
	
	/**
	 * Modifies the given stats object by the inverse of what is indicated by
	 * this modifier object. Useful for removing items.
	 * @param	stats			The stats to unapply to
	 * @param	r				The portion to unapply
	 */
	public void unapplyTo(Stats stats, float r) {
		apply(stats, -r);
	}
	
	/**
	 * Modifies the given stats object.
	 * @param	stats			The stats to mutate
	 * @param	sign			The multiplier for this operation
	 */
	protected void apply(Stats stats, float sign) {
		stats.hp			+= (sign * mdo.hp);
		stats.mhp			+= (sign * mdo.mhp);
		stats.mp			+= (sign * mdo.mp);
		stats.mmp			+= (sign * mdo.mmp);
		stats.speed			+= (sign * mdo.speed);
		stats.vision		+= (sign * mdo.vision);
		stats.dmgBase		+= (sign * mdo.dmgBase);
		stats.dmgRange		+= (sign * mdo.dmgRange);
		stats.defense		+= (sign * mdo.dodge);
		stats.armor			+= (sign * mdo.armor);
		stats.magBase		+= (sign * mdo.magBase);
		stats.magRange		+= (sign * mdo.magRange);
		stats.magArmor		+= (sign * mdo.magArmor);
		stats.walkEP		+= (sign * mdo.walkCost);
		stats.attackEP		+= (sign * mdo.attackCost);
		
		if (stats.hp < 0) stats.hp = 0;
		if (stats.hp > stats.mhp) stats.hp = (stats.mhp);
		if (stats.mp < 0) stats.mp = 0;
		if (stats.mp > stats.mmp) stats.mp = (stats.mmp);
	}

}
