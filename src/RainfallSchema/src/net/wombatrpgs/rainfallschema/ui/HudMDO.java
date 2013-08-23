/**
 *  HudMDO.java
 *  Created on Feb 6, 2013 1:34:26 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
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
	
	// Eventually stuff should go here about charas to track
	
	@Desc("Frame graphic")
	@FileLink("ui")
	public String frameGraphic;
	
	@Desc("Frame width")
	public Integer frameWidth;
	
	@Desc("Frame height")
	public Integer frameHeight;
	
	@Desc("Hotizontal offset, in px")
	public Integer offX;
	
	@Desc("Vertical offset, in px")
	public Integer offY;
	
	@Desc("HP bar base graphic")
	@FileLink("ui")
	public String hpBaseGraphic;
	
	@Desc("HP bar rib graphic")
	@FileLink("ui")
	public String hpRibGraphic;
	
	@Desc("HP bar tail graphic")
	@FileLink("ui")
	public String hpTailGraphic;
	
	@Desc("HP bar length when at 100% hp, in px")
	public Integer hpWidth;
	
	@Desc("X coord where HP bar stretch starts, relative to bottom left, in px")
	public Integer hpStartX;
	
	@Desc("Y coord where HP bar stretch starts, relative to bottom left, in px")
	public Integer hpStartY;
	
	@Desc("MP bar base graphic")
	@FileLink("ui")
	public String mpBaseGraphic;
	
	@Desc("MP bar rib graphic")
	@FileLink("ui")
	public String mpRibGraphic;
	
	@Desc("MP bar tail graphic")
	@FileLink("ui")
	public String mpTailGraphic;
	
	@Desc("MP bar length when at 100% mp, in px")
	public Integer mpWidth;
	
	@Desc("X coord where MP bar stretch starts, relative to bottom left, in px")
	public Integer mpStartX;
	
	@Desc("Y coord where MP bar stretch starts, relative to bottom left, in px")
	public Integer mpStartY;

}
