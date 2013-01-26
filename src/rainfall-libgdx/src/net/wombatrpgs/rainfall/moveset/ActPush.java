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
import net.wombatrpgs.rainfallschema.hero.moveset.PushMDO;

/**
 * Push or pull the block.
 */
public class ActPush extends MovesetAct {
	
	protected PushMDO mdo;
	protected boolean keyDown;
	protected boolean active;

	/**
	 * Constructs a new push act from data.
	 * @param	actor			The character performing the action
	 * @param 	mdo				The MDO to construct from
	 */
	public ActPush(CharacterEvent actor, PushMDO mdo) {
		super(actor, mdo);
		this.mdo = mdo;
		this.keyDown = false;
		this.active = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, CharacterEvent actor) {
		if (RGlobal.block == null) return;
		if (RGlobal.block.getLevel() != RGlobal.hero.getLevel()) return;
		int compX = 0;
		int compY = 0;
		if (!keyDown) {
			keyDown = true;
			if (RGlobal.hero.isActing()) return;
			active = true;
			RGlobal.hero.setActing(true);
			animFromMDO.reset();
			int dx = RGlobal.hero.getX() - RGlobal.block.getX();
			int dy = RGlobal.hero.getY() - RGlobal.block.getY();
			if (Math.abs(dx) > Math.abs(dy)) {
				compX = 1;
			} else {
				compY = 1;
			}
			if (RGlobal.hero.getX() > RGlobal.block.getX()) compX *= -1;
			if (RGlobal.hero.getY() > RGlobal.block.getY()) compY *= -1;
			compX *= mdo.targetVelocity;
			compY *= mdo.targetVelocity;
			setAppearance(this.animFromMDO);
			RGlobal.hero.halt();
			RGlobal.hero.getAppearance().startMoving();
		} else {
			keyDown = false;
			if (!active) return;
			restoreAppearance();
			RGlobal.hero.setActing(false);
		}
		RGlobal.block.setVelocity(compX, compY);
		RGlobal.block.setMoving(keyDown);
	}

}
