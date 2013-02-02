/**
 *  GraphicMDO.java
 *  Created on Feb 2, 2013 4:15:10 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * UI graphics.
 */
@Path("ui/")
public class GraphicMDO extends MainSchema {
	
	@Desc("File - location of the graphic file, .png")
	@FileLink("ui")
	public String file;
	
	@Desc("Width, in px")
	public Integer width;
	
	@Desc("Height, in px")
	public Integer height;

}
