/**
 *  NinesliceMDO.java
 *  Created on Jan 17, 2014 2:24:06 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.ui;

import net.wombatrpgs.mgneschema.ui.data.NinesliceScaleType;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Thing for UI: corners and edges.
 */
@Path("ui/")
public class NinesliceMDO extends MainSchema {
	
	@Desc("File with the nineslice")
	@FileLink("ui")
	public String file;
	
	@Desc("How to resize the slice")
	public NinesliceScaleType type;
	
	@Desc("Gradient box - will replace interior slice if exists")
	@SchemaLink(GradientBoxMDO.class)
	@Nullable
	public String gradient;
	
	@Desc("Width of one of the slices")
	public Integer sliceWidth;
	
	@Desc("Height of one of the slices")
	public Integer sliceHeight;

}
