/**
 *  ActPush.java
 *  Created on Dec 29, 2012 12:44:28 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.hero.moveset.PushMDO;

/**
 * Push or pull the block.
 */
public class ActPush implements Actionable {
	
	protected PushMDO mdo;
	protected boolean active;

	public ActPush(PushMDO mdo) {
		this.mdo = mdo;
		this.active = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void act(Level map) {
		if (RGlobal.block == null) return;
		if (RGlobal.block.getLevel() != RGlobal.hero.getLevel()) return;
		int compX = 0;
		int compY = 0;
		if (!active) {
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
		}
		RGlobal.hero.setVelocity(0, 0);
		RGlobal.block.setVelocity(compX, compY);
		active = !active;
		RGlobal.block.setMoving(active);
	}

}
