/**
 *  EventCeilingMDO.java
 *  Created on Sep 23, 2014 8:21:07 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.events;

import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;

/**
 * An on-the-fly room, except not so on-the-fly.
 */
public class EventCeilingMDO extends EventMDO {
	
	@Desc("Roof ID - the tile ID of the chip to use for the roof")
	public Integer roofID;
	
	@Desc("Roof tileset - the name of the tileset the roof tile is found on")
	public String roofTileset;

}
