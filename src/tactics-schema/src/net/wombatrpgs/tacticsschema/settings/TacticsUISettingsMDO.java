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

/**
 * UI settings specific to Tactics.
 */
@Path("settings/")
public class TacticsUISettingsMDO extends MainSchema {
	
	@Desc("Map highlight - semi-transparent thing showed on walkable squares")
	@FileLink("ui")
	public String mapHighlight;
	
	@Desc("Cursor")
	@SchemaLink(CursorMDO.class)
	public String cursor;

}
