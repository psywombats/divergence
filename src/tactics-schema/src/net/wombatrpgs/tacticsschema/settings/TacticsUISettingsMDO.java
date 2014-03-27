/**
 *  TacticsUISettings.java
 *  Created on Feb 12, 2014 11:26:55 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.tacticsschema.ui.CursorMDO;
import net.wombatrpgs.tacticsschema.ui.DirectionSelectorMDO;

/**
 * UI settings specific to Tactics.
 */
@Path("settings/")
public class TacticsUISettingsMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;

	@Desc("Map highlight - semi-transparent thing showed on walkable squares")
	@FileLink("ui")
	public String mapHighlight;
	
	@Desc("Cursor - displays on the map so it can animate")
	@SchemaLink(CursorMDO.class)
	public String cursor;
	
	@Desc("Direction targeter - overlays for selecting 1-9 direction")
	@SchemaLink(DirectionSelectorMDO.class)
	public String target;

}
