/**
 *  IconPlacementMDO.java
 *  Created on Feb 6, 2013 1:43:44 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.ui.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * An entry for the HUD that dictates where to place an icon.
 */
@ExcludeFromTree
public class IconPlacementMDO extends HeadlessSchema {
	
	@Desc("Command - when a move is bound to this command, its icon will appear here")
	public InputCommand command;
	
	@Desc("Offset X - icon will be offset locally by this many pixels from left")
	public Integer offX;
	
	@Desc("Offset Y - icon will be offset locally by this many pixels from top")
	public Integer offY;

}
