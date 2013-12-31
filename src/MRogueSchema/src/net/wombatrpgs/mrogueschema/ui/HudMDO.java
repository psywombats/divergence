/**
 *  HudMDO.java
 *  Created on Feb 6, 2013 1:34:26 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * A graphic that is display on top of the screen.
 */
@Path("ui/")
public class HudMDO extends MainSchema {
	
	@Desc("Anchor side - graphic will be pinned to this side of the screen")
	public OrthoDir anchorDir;
	
	@Desc("Font - for others")
	@SchemaLink(FontMDO.class)
	public String font;
	
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
	
	@Desc("Display update speed - When a number changes on the HUD, there's a "
			+ "wait time of this many seconds between each decrement to the "
			+ "displayed numbers, zero for instant change")
	public Float digitDelay;
	
	@Desc("MP display update speed - same as above")
	public Float mpDigitDelay;
	
	@Desc("Number set for HP numbers")
	@SchemaLink(NumberSetMDO.class)
	public String numberSet;
	
	@Desc("Number set for MP numbers")
	@SchemaLink(NumberSetMDO.class)
	public String mpNumberSet;
	
	@Desc("X offset of HP numbers, in px from lower left")
	public Integer numOffX;
	
	@Desc("Y offset of HP numbers, in px from lower left")
	public Integer numOffY;
	
	@Desc("X offset of MP numbers, in px from lower left")
	public Integer numMPOffX;
	
	@Desc("Y offset of MP numbers, in px from lower left")
	public Integer numMPOffY;
	
	@Desc("HP bar base graphic")
	@FileLink("ui")
	public String hpBaseGraphic;
	
	@Desc("HP bar rib graphic")
	@FileLink("ui")
	public String hpRibGraphic;
	
	@Desc("HP bar tail graphic")
	@FileLink("ui")
	public String hpTailGraphic;
	
	@Desc("No HP bar base graphic")
	@FileLink("ui")
	public String nhpBaseGraphic;
	
	@Desc("No HP bar rib graphic")
	@FileLink("ui")
	public String nhpRibGraphic;
	
	@Desc("No HP bar tail graphic")
	@FileLink("ui")
	public String nhpTailGraphic;
	
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
	
	@Desc("No MP bar base graphic")
	@FileLink("ui")
	public String nmpBaseGraphic;
	
	@Desc("No MP bar rib graphic")
	@FileLink("ui")
	public String nmpRibGraphic;
	
	@Desc("No MP bar tail graphic")
	@FileLink("ui")
	public String nmpTailGraphic;
	
	@Desc("MP bar length when at 100% mp, in px")
	public Integer mpWidth;
	
	@Desc("X coord where MP bar stretch starts, relative to bottom left, in px")
	public Integer mpStartX;
	
	@Desc("Y coord where MP bar stretch starts, relative to bottom left, in px")
	public Integer mpStartY;
	
	@Desc("Head alpha mask")
	@FileLink("ui")
	public String alphaMask;
	
	@Desc("Head x, from bottom left")
	public Integer headX;
	
	@Desc("Head y, from bottom left")
	public Integer headY;

}
