/**
 *  ShaderMDO.java
 *  Created on Apr 18, 2013 10:09:24 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * A shader! Programmable pipeline! Has two parts!
 */
@Path("graphics/")
public class ShaderMDO extends MainSchema {
	
	@Desc("Vertex shader")
	@FileLink("shaders")
	public String vertexFile;

	@Desc("Fragment shader")
	@FileLink("shaders")
	public String fragmentFile;

}
