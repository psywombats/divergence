/**
 *  SkillsBox.java
 *  Created on Oct 19, 2013 1:55:02 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * That box that supports things like uh... pop-up skills
 */
@Path("ui/")
public class SkillsBoxMDO extends MainSchema {
	
	@Desc("Font - font used for things like items remaining, ")
	@SchemaLink(FontMDO.class)
	public String font;
	
	@Desc("Empty - icon to use when there's no ability")
	@FileLink("ui")
	@Nullable
	public String empty;
	
	@Desc("Anchor - How to anchor this piece of UI... might do nothing")
	public AnchorType anchor;
	
	@Desc("Offset x - to start at, in px from bottom left")
	public Integer allOffX;
	
	@Desc("Offset y - to start at, in px from bottom left")
	public Integer allOffY;
	
	@Desc("Spacing - The pixels between any two icons")
	public Integer paddingX;
	
	@Desc("Text x - Offset from each icon to draw text at, in px from center of icon")
	public Integer textY;
	
	@Desc("Text Y - Offset from each icon to draw text at, in px from bottom left")
	public Integer textX;

}
