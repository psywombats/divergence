/**
 *  PaletteMDO.java
 *  Created on May 15, 2014 3:38:17 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Gameboy/GBC palette thing for Saga graphics shaders.
 */
@Path("graphics/")
public class PaletteMDO extends MainSchema {
	
	@Desc("Output black - output color shaders use as the gameboy black, use rrr,ggg,bbb format")
	public String outBlack;
	@Desc("Output dark gray - output color shaders use as the gameboy dark gray, use rrr,ggg,bbb format")
	public String outDgray;
	@Desc("Output light gray - output color shaders use as the gameboy light gray, use rrr,ggg,bbb format")
	public String outLgray;
	@Desc("Output white - output color shaders use as the gameboy white, use rrr,ggg,bbb format")
	public String outWhite;

}
