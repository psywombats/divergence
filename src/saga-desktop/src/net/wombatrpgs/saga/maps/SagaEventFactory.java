/**
 *  SagaEventFactory.java
 *  Created on Jun 26, 2014 12:51:17 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.maps.events.MapEvent;

/**
 * Saga-specific map events.
 */
public class SagaEventFactory extends EventFactory {
	
	protected static final String TYPE_ENCOUNTER = "Encounter";

	/**
	 * @see net.wombatrpgs.mgne.maps.events.EventFactory#createEvent
	 * (net.wombatrpgs.mgne.maps.TiledMapObject)
	 */
	@Override
	protected MapEvent createEvent(TiledMapObject object) {
		String type = object.getString(TiledMapObject.PROPERTY_TYPE);
		if (type.equals(TYPE_ENCOUNTER)) {
			return new EventEncounter(object);
		}
		return super.createEvent(object);
	}

}
