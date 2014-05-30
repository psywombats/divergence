/**
 *  EffectStatCandy.java
 *  Created on May 30, 2014 11:59:12 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.screen.TargetSelectable;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectStatCandyMDO;

/**
 * HP200, Strong, etc.
 */
public class EffectStatCandy extends EffectBattleUnuseable {
	
	protected EffectStatCandyMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectStatCandy(EffectStatCandyMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isMapUsable() */
	@Override public boolean isMapUsable() { return true; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#onMapUse
	 * (net.wombatrpgs.saga.screen.TargetSelectable)
	 */
	@Override
	public void onMapUse(final TargetSelectable caller) {
		caller.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected != null) {
					if (mdo.restrictRace != null &&
							selected.getRace() != mdo.restrictRace) {
						// TODO: sfx: fail sfx
					} else if (mdo.maxValue != 0 &&
							selected.get(mdo.stat) > mdo.maxValue) {
						// TODO: sfx: fail sfx
						// TODO: polish: maybe print an error message?
					} else {
						// TODO: sfx: stat gain sfx
						int gain = MGlobal.rand.nextInt(mdo.maxGain - mdo.minGain);
						gain += mdo.minGain;
						selected.modifyStat(mdo.stat, gain);
						caller.refresh();
					}
				}
				return true;
			}
		});
	}

}
