/**
 *  SagaGraphicsMDO.java
 *  Created on May 15, 2014 1:16:38 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * SaGa-specific graphics settings.
 */
@Path("graphics/")
public class SagaGraphicsMDO extends MainSchema {
	
	@Desc("Filter black - what shaders treat as black in source graphics, use rrr,ggg,bbb format")
	public String filterBlack;
	@Desc("Filter dark gray - what shaders treat as dark gray in source graphics, use rrr,ggg,bbb format")
	public String filterDgray;
	@Desc("Filter light gray - what shaders treat as light gray in source graphics, use rrr,ggg,bbb format")
	public String filterLgray;
	@Desc("Filter white - what shaders treat as white in source graphics, use rrr,ggg,bbb format")
	public String filterWhite;
	
	@Desc("Output black - output color shaders use as the gameboy black, use rrr,ggg,bbb format")
	public String outBlack;
	@Desc("Output dark gray - output color shaders use as the gameboy dark gray, use rrr,ggg,bbb format")
	public String outDgray;
	@Desc("Output light gray - output color shaders use as the gameboy light gray, use rrr,ggg,bbb format")
	public String outLgray;
	@Desc("Output white - output color shaders use as the gameboy white, use rrr,ggg,bbb format")
	public String outWhite;

}
