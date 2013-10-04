/**
 *  FontMDO.java
 *  Created on Feb 2, 2013 2:27:11 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * Fonts for use in cutscenes.
 */
@Path("ui/")
public class FontMDO extends MainSchema {
	
	@Desc("File - the .fnt file used by this font")
	@FileLink("fonts")
	public String file;

}
