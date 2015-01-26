/**
 *  EventStalker.java
 *  Created on Jan 25, 2015 12:55:46 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Timer;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * bacons stalker
 */
public class EventStalker extends MapEvent {
	
	protected boolean wandering;

	public EventStalker(EventMDO mdo) {
		super(mdo);
		this.maxVelocity = 32;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (distanceTo(MGlobal.getHero()) < 300) {
			wandering = false;
			targetLocation(MGlobal.getHero().getX(), MGlobal.getHero().getY());
			if (Math.abs(vx) > Math.abs(vy)) {
				if (vx < 0) {
					setFacing(OrthoDir.WEST);
				} else {
					setFacing(OrthoDir.EAST);
				}
			} else {
				if (vy < 0) {
					setFacing(OrthoDir.SOUTH);
				} else {
					setFacing(OrthoDir.NORTH);
				}
			}
		} else if (!wandering) {
			new Timer(2f, new FinishListener() {
				@Override public void onFinish() {
					if (wandering) {
						wandering = false;
						targetLocation(
								getX() + MGlobal.rand.nextInt(64) - 32,
								getY() + MGlobal.rand.nextInt(64) - 32);
					}
				}
			});
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.mgne.maps.events.MapEvent, net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent event, CollisionResult result) {
		if (event == MGlobal.getHero()) {
			new Timer(0f, new FinishListener() {
				@Override public void onFinish() {
					MGlobal.levelManager.getTele().teleport(
							MGlobal.memory.levelKey,
							MGlobal.memory.heroMemory.tileX,
							MGlobal.memory.heroMemory.tileY);
				}
			});
		}
		return super.onCollide(event, result);
	}

}
