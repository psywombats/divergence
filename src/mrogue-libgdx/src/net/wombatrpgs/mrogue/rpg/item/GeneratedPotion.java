/**
 *  GeneratedPotion.java
 *  Created on Oct 22, 2013 5:22:28 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.StatsModifier;
import net.wombatrpgs.mrogueschema.items.GeneratedPotionMDO;
import net.wombatrpgs.mrogueschema.items.PotionPrefixMDO;
import net.wombatrpgs.mrogueschema.items.PotionTypeMDO;

/**
 * A potion that was pieced together from the potion generator.
 */
public class GeneratedPotion extends Item {
	
	protected PotionPrefixMDO preMDO;
	protected PotionTypeMDO typeMDO;

	/**
	 * Generates a potion randomly from data.
	 * @param	mdo				The data to generate from
	 */
	public GeneratedPotion(GeneratedPotionMDO mdo) {
		super(mdo);
		this.preMDO = MGlobal.data.getEntryFor(
				mdo.prefixes[MGlobal.rand.nextInt(mdo.prefixes.length)],
				PotionPrefixMDO.class);
		this.typeMDO = MGlobal.data.getEntryFor(
				mdo.types[MGlobal.rand.nextInt(mdo.types.length)],
				PotionTypeMDO.class);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.item.Item#internalUse()
	 */
	@Override
	protected void internalUse() {
		if (MGlobal.hero.inLoS(owner.getParent())) {
			MGlobal.ui.getNarrator().msg(owner.getName() + " drinks " + getName() + ".");
		}
		final GameUnit user = owner;
		final StatsModifier mod = new StatsModifier(preMDO.stats);
		final float r = typeMDO.rate;
		mod.applyTo(user.getStats(), r);
		user.ensureAlive();
		if (user.isDead()) return;
		if (typeMDO.duration > 0) {
			owner.addTurnChild(new Turnable() {
				private int countdown = typeMDO.duration;
				@Override public void onTurn() {
					countdown -= 1;
					if (countdown == 0) {
						if (user == MGlobal.hero.getUnit()) {
							MGlobal.ui.getNarrator().msg("The effects of " +
										getName() + " wear off.");
						}
						mod.unapplyTo(user.getStats(), r);
						user.removeTurnChild(this);
						user.ensureAlive();
					}
				}
			});
		}
		final StatsModifier mod2 = new StatsModifier(typeMDO.extraStats);
		mod2.applyTo(user.getStats());
		user.ensureAlive();
		if (typeMDO.extraDuration > 0) {
			owner.addTurnChild(new Turnable() {
				private int countdown = typeMDO.extraDuration;
				@Override public void onTurn() {
					countdown -= 1;
					if (countdown == 0) {
						if (user == MGlobal.hero.getUnit()) {
							MGlobal.ui.getNarrator().msg("The side effects " +
										"of " + getName() + " wear off.");
						}
						mod2.unapplyTo(user.getStats());
						user.removeTurnChild(this);
						user.ensureAlive();
					}
				}
			});
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.item.Item#getName()
	 */
	@Override
	public String getName() {
		return "a " + preMDO.prename + " " + typeMDO.typename;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.item.Item#getDescription()
	 */
	@Override
	public String getDescription() {
		return preMDO.predesc + " " + typeMDO.typedesc;
	}

}
