/**
 *  EventCheckpoint.java
 *  Created on Jan 24, 2015 3:36:44 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.RectHitbox;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * bacon
 */
public class EventCheckpoint extends MapEvent {
	
	protected RectHitbox rect;

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
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.mgne.maps.events.MapEvent, net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent event, CollisionResult result) {
		if (event == MGlobal.getHero()) {
			System.out.println("Hero!");
		}
		return super.onCollide(event, result);
	}

}
