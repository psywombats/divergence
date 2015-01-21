/**
 *  GeometryEvent.java
 *  Created on Jan 20, 2015 2:04:10 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.RectHitbox;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * Bacon event for wall ceiling things? Not sure. Assumed rectangular.
 */
public class EventGeometry extends MapEvent {
	
	protected RectHitbox rect;

	public EventGeometry(EventMDO mdo, TiledMapObject object) {
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

}
