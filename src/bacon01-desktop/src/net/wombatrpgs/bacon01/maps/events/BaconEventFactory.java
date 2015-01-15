/**
 *  BaconEventFactory.java
 *  Created on Jan 15, 2015 1:04:10 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.sagaschema.events.EventLightMDO;

/**
 * Generates bacon-specific events.
 */
public class BaconEventFactory extends EventFactory {
	
	protected static final String TYPE_LIGHT = "light";

	/**
	 * @see net.wombatrpgs.mgne.maps.events.EventFactory#createEvent
	 * (net.wombatrpgs.mgne.maps.TiledMapObject)
	 */
	@Override
	protected MapEvent createEvent(TiledMapObject object) {
		String type = object.getString(TiledMapObject.PROPERTY_TYPE);
		if (TYPE_LIGHT.equals(type)) {
			return new EventLight(object.generateMDO(EventLightMDO.class));
		}
		return super.createEvent(object);
	}
}
