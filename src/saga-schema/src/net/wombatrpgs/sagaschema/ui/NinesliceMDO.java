/**
 *  NinesliceMDO.java
 *  Created on Jan 17, 2014 2:24:06 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.sagaschema.ui.data.NinesliceType;

/**
 * Thing for UI: corners and edges.
 */
@Path("ui/")
public class NinesliceMDO extends MainSchema {
	
	@Desc("File with the nineslice")
	@FileLink("ui")
	public String file;
	
	@Desc("How to resize the slice")
	public NinesliceType type;
	
	@Desc("Width of one of the slices")
	public Integer sliceWidth;
	
	@Desc("Height of one of the slices")
	public Integer sliceHeight;

}
