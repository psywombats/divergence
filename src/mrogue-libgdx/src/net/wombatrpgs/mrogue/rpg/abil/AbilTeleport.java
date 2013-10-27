/**
 *  AbilTeleport.java
 *  Created on Oct 27, 2013 6:03:08 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.travel.StepMove;
import net.wombatrpgs.mrogueschema.characters.effects.AbilTeleportMDO;

/**
 * Teleports some fool.
 */
public class AbilTeleport extends AbilEffect {
	
	protected AbilTeleportMDO mdo;

	/**
	 * Constructs a teleport ability given data, parent
	 * @param	mdo				The data to use to generate
	 * @param	abil			The ability to generate for
	 */
	public AbilTeleport(AbilTeleportMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.abil.AbilEffect#internalAct(java.util.List)
	 */
	@Override
	protected void internalAct(List<GameUnit> targets) {
		for (GameUnit target : targets) {
			CharacterEvent chara = target.getParent();
			String realName = target.getName();
			boolean seen1 = MGlobal.hero.inLoS(chara);
			int baseX = chara.getTileX();
			int baseY = chara.getTileY();
			int tries = 0;
			while (chara.euclideanTileDistanceTo(baseX, baseY) < mdo.scatterMin ||
					chara.euclideanTileDistanceTo(baseX, baseY) > mdo.scatterMax ||
					!chara.getParent().isTilePassable(chara, chara.getTileX(), chara.getTileY())) {
				chara.setTileX((int) (baseX + MGlobal.rand.nextInt((int) (mdo.scatterMax*2)) - mdo.scatterMax));
				chara.setTileY((int) (baseY + MGlobal.rand.nextInt((int) (mdo.scatterMax*2)) - mdo.scatterMax));
				tries += 1;
				if (tries >= 1000) break;
			}
			chara.flash(new Color(1, 1, 1, 0), MGlobal.constants.getDelay()*1.2f);
			chara.addStep(new StepMove(chara, chara.getTileX(), chara.getTileY()));
			if (tries == 1000) {
				MGlobal.reporter.warn("Teleport took too long " + target);
				chara.setTileX(baseX);
				chara.setTileY(baseY);
			}
			boolean seen2 = MGlobal.hero.inLoS(chara);
			if (seen1) {
				if (seen2) {
					MGlobal.ui.getNarrator().msg(target.getName() + " teleported.");
				} else {
					MGlobal.ui.getNarrator().msg(realName + " vanished.");
				}
			} else if (seen2) {
				MGlobal.ui.getNarrator().msg(target.getName() + " appeared out of nowhere.");
			}
		}
	}

}
