/**
 *  TextBoxMDO.java
 *  Created on Feb 2, 2013 3:35:57 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.ui;

import net.wombatrpgs.mgneschema.ui.data.BoxAnchorType;
import net.wombatrpgs.mgneschema.ui.data.TextScaleType;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Defines a textbox.
 */
@Path("ui/")
public class TextBoxMDO extends MainSchema {
	
	@Desc("Anchor type - where this displays on page")
	public BoxAnchorType anchor;
	
	@Desc("Scale type - whether box should scale with viewport")
	public TextScaleType scaling;
	
	@Desc("Nineslice - will be stretched to form backer for box")
	@SchemaLink(NinesliceMDO.class)
	@Nullable
	public String nineslice;
	
	@Desc("Text autotype speed - in characters per second")
	public Integer typeSpeed;
	
	@Desc("Type sfx reference key - plays once per character autotyped,"
			+ "corresponds to entry in sound manager, or empty")
	public String typeRefKey;
	
	@Desc("Line count")
	public Integer lines;
	
	@Desc("Margin width - Pixel amount on each side from edge of the screen")
	public Integer marginWidth;
	
	@Desc("Margin top - Pixel amount on top from edge of the screen")
	public Integer marginTop;
	
	@Desc("Margin bottom - Pixel amount on bottom from edge of the screen")
	public Integer marginBottom;

}
