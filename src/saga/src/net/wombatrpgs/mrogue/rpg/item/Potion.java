/**
 *  Potion.java
 *  Created on Oct 21, 2013 4:24:37 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.StatsModifier;
import net.wombatrpgs.mrogueschema.items.PotionMDO;

/**
 * General purpose consumable.
 */
public class Potion extends Item {
	
	protected PotionMDO mdo;

	/**
	 * Creates a new potion from data.
	 * @param	mdo				The data to generate from
	 */
	public Potion(PotionMDO mdo) {
		super(mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.item.Item#internalUse()
	 */
	@Override
	protected void internalUse() {
		if (MGlobal.hero.inLoS(owner.getParent())) {
			MGlobal.ui.getNarrator().msg(owner.getName() + " drinks " + getName() + ".");
		}
		final StatsModifier mod = new StatsModifier(mdo.mod);
		mod.applyTo(owner.getStats());
		owner.ensureAlive();
		if (mdo.duration > 0) {
			final GameUnit user = owner;
			owner.addTurnChild(new Turnable() {
				private int countdown = mdo.duration;
				@Override public void onTurn() {
					countdown -= 1;
					if (countdown == 0) {
						if (user == MGlobal.hero.getUnit()) {
							MGlobal.ui.getNarrator().msg("The effects of " +
										getName() + " wear off.");
						}
						mod.unapplyTo(user.getStats());
						user.removeTurnChild(this);
						user.ensureAlive();
					}
				}
			});
		}
	}

}
