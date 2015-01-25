/**
 *  EventCheckpoint.java
 *  Created on Jan 24, 2015 3:36:44 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import net.wombatrpgs.bacon01.core.BMemory;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.RectHitbox;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * bacon
 */
public class EventCheckpoint extends MapEvent {
	
	protected RectHitbox rect;
	protected Graphic graphic;
	protected boolean collidedLast;

	public EventCheckpoint(EventMDO mdo, TiledMapObject object) {
		super(mdo);
		rect = object.getRectHitbox();
		rect.setParent(this);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return rect;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		boolean colliding = MGlobal.getHero().getHitbox().isColliding(getHitbox()).isColliding;
		if (colliding && !collidedLast) {
			MGlobal.memory.save(BMemory.FILE_NAME);
		}
		collidedLast = colliding;
	}

}
