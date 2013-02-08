/**
 *  ActPush.java
 *  Created on Dec 29, 2012 12:44:28 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.PushMDO;

/**
 * Push or pull the block.
 */
public class ActPush extends MovesetAct {
	
	protected PushMDO mdo;
	protected boolean anulled;

	/**
	 * Constructs a new push act from data.
	 * @param	actor			The character performing the action
	 * @param 	mdo				The MDO to construct from
	 */
	public ActPush(CharacterEvent actor, PushMDO mdo) {
		super(actor, mdo);
		this.mdo = mdo;
		anulled = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, CharacterEvent actor) {
		int compX = 0;
		int compY = 0;
		if (!actor.isMoveActive(this)) {
			if (RGlobal.block == null || RGlobal.block.getLevel() != RGlobal.hero.getLevel()) {
				anulled = !anulled;
				return;
			}
			if (anulled) {
				anulled = !anulled;
				return;
			}
			if (map.getZ(actor) == map.getZ(RGlobal.block)+1 &&
					RGlobal.block.getUpperBox().isColliding(actor.getHitbox()).isColliding) {
				return;
			}
			actor.halt();
			actor.startAction(this);
			actor.faceToward(RGlobal.block);
			int dx = actor.getX() - RGlobal.block.getX();
			int dy = actor.getY() - RGlobal.block.getY();
			if (Math.abs(dx) > Math.abs(dy)) {
				compX = 1;
			} else {
				compY = 1;
			}
			if (RGlobal.hero.getX() > RGlobal.block.getX()) compX *= -1;
			if (RGlobal.hero.getY() > RGlobal.block.getY()) compY *= -1;
			compX *= mdo.targetVelocity;
			compY *= mdo.targetVelocity;
		} else {
			actor.stopAction(this);
		}
		RGlobal.block.targetVelocity(compX, compY);
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.MovesetAct#cancel()
	 */
	@Override
	public void cancel() {
		super.cancel();
		RGlobal.block.targetVelocity(0, 0);
	}

}
