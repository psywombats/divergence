/**
 *  ShaderTest.java
 *  Created on Apr 18, 2013 7:24:42 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.test;

import net.wombatrpgs.mgneschema.test.data.TestState;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Shade everything like this.
 */
@Path("test/")
public class ShaderTestMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;

	public TestState enabled;
	
	@Desc("Vertex shader")
	@FileLink("shaders")
	public String vertexFile;

	@Desc("Fragment shader")
	@FileLink("shaders")
	public String fragmentFile;
}
