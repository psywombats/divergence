/**
 *  IntroSettingsMDO.java
 *  Created on Feb 22, 2013 4:36:25 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;
import net.wombatrpgs.mrogueschema.maps.MapMDO;

/**
 * What happens when the game starts.
 */
@Path("settings/")
public class IntroSettingsMDO extends MainSchema {
	
	@Desc("Immediate scene - the scene to play on arrival to title")
	@SchemaLink(SceneParentMDO.class)
	public String immScene;
	
	@Desc("Title start scene - the scene script to play on the title screen")
	@SchemaLink(SceneParentMDO.class)
	public String titleScene;
	
	@Desc("Game enter scene - the scene script to play on entering game scene")
	@SchemaLink(SceneParentMDO.class)
	public String scene;
	
	@Desc("Map - the map that things open on, usually a blank screen with the hero on it")
	@SchemaLink(MapMDO.class)
	public String map;

}
