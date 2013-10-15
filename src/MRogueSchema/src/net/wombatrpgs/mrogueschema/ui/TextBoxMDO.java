/**
 *  TextBoxMDO.java
 *  Created on Feb 2, 2013 3:35:57 AM for project RainfallSchema
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
import net.wombatrpgs.mrogueschema.audio.SoundMDO;

/**
 * Defines a textbox.
 */
@Path("ui/")
public class TextBoxMDO extends MainSchema {
	
	@Desc("Anchor type, like where this displays on page")
	public AnchorType anchor;
	
	@Desc("Graphic - ui box thing displayed beneath the text")
	@FileLink("ui")
	@Nullable
	public String image;
	
	@Desc("No-name Graphic - displayed like the ui box when speaker has no name")
	@FileLink("ui")
	@Nullable
	public String image2;
	
	@Desc("Box x - upper left x of where the text box image is displayed (in px)")
	public Integer graphicX;
	
	@Desc("Box y - upper left y of where the text box image is displayed (in px), " +
			"refers to bottom of text")
	public Integer graphicY;
	
	@Desc("x1 - upper left x of the rectangle where text will be (in px)")
	public Integer x1;
	
	@Desc("y1 - upper left y of the rectangle where text will be (in px), " +
			"refers to bottom of text")
	public Integer y1;
	
	@Desc("x2 - lower right x of the rectangle where text will be (in px)")
	public Integer x2;
	
	@Desc("y2 - lower right y of the rectangle where text will be (in px)")
	public Integer y2;
	
	@Desc("Name x - upper left x of where character's name is printed, if used (in px)")
	public Integer nameX;
	
	@Desc("Name y - upper left x of where character's name is printed, if used (in px), " +
			"refers to bottom of text")
	public Integer nameY;
	
	@Desc("Text autotype speed - in characters per second")
	public Integer typeSpeed;
	
	@Desc("Type sfx - plays once per character autotyped")
	@SchemaLink(SoundMDO.class)
	@Nullable
	public String typeSfx;

}
