/**
 *  HudMDO.java
 *  Created on Feb 6, 2013 1:34:26 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * A graphic that is display on top of the screen.
 */
@Path("ui/")
public class HudMDO extends MainSchema {
	
	@Desc("Anchor side - graphic will be pinned to this side of the screen")
	public Direction anchorDir;

}
