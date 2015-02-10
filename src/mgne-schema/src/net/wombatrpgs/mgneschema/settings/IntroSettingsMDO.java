/**
 *  IntroSettingsMDO.java
 *  Created on Feb 22, 2013 4:36:25 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * What happens when the game starts.
 */
@Path("settings/")
public class IntroSettingsMDO extends MainSchema {
	
	@Desc("Map - the map that things open on, usually a blank screen with the hero on it")
	@FileLink("maps")
	public String map;
	
	@Desc("Start x - tile on the map where hero starts, x-coord")
	public Integer mapX;
	
	@Desc("Start y - tile on the map where the hero starts, y-coord")
	public Integer mapY;
	
	@Desc("Title bg - title background image")
	@FileLink("ui")
	public String titleBG;
	
	@Desc("Glow cycles - moon will flash green this many times")
	public Integer glowCycles;
	
	@Desc("Explode time - explosion effect happens at this time")
	public Float timeExplode;
	
	@Desc("Skyrip time - static effect starts playing at this time")
	public Float timeSkyrip;
	
	@Desc("Glow time - glowing effect starts playing at this time")
	public Float timeGlow;
	
	@Desc("Wait2 time - glowing effect ends at this time")
	public Float timeWait2;
	
	@Desc("Flying time - ship starts flying in at this time")
	public Float timeFlyin;
	
	@Desc("Landing time - ship thrusters turn off at this time")
	public Float timeLand;
	
	@Desc("Wait3 time - ship is on the ground at this time")
	public Float timeWait3;
	
	@Desc("End time - screen starts fading out at this time")
	public Float timeEnd;
	
	

}
