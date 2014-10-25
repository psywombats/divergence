/**
 *  EffectStatCandy.java
 *  Created on May 30, 2014 11:59:12 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.Arrays;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.screen.TargetSelectable;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectStatCandyMDO;

/**
 * HP200, Strong, etc.
 */
public class EffectStatCandy extends EffectBattleUnusable {
	
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
					List<Race> allowed = Arrays.asList(mdo.restrictRace);
					if (!allowed.contains(selected.getRace())) {
						MGlobal.audio.playSFX(SConstants.SFX_FAIL);
					} else if (mdo.maxValue != 0 &&
							selected.get(mdo.stat) > mdo.maxValue) {
						MGlobal.audio.playSFX(SConstants.SFX_FAIL);
						// TODO: polish: maybe print an error message?
					} else {
						MGlobal.audio.playSFX(SConstants.SFX_CURE);
						int delta = mdo.maxGain - mdo.minGain;
						if (delta > 0) delta = MGlobal.rand.nextInt(delta);
						selected.modifyStat(mdo.stat, (mdo.minGain + delta));
						getItem().deductUse();
						caller.refresh();
					}
				}
				return true;
			}
		});
	}

}
