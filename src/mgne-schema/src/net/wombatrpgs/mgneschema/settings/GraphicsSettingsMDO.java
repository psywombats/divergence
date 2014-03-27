/**
 *  GraphicsSettingsMDO.java
 *  Created on Sep 3, 2013 5:28:49 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings;

import net.wombatrpgs.mgneschema.graphics.data.EffectEnabledType;
import net.wombatrpgs.mgneschema.settings.data.ShaderEnabledState;
import net.wombatrpgs.mgneschema.test.data.TestState;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Controls some global graphics configurationy things.
 */
@Path("graphics/")
public class GraphicsSettingsMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;

	@Desc("Chunking - (advanced) enables/disables the character order rendering algorithm")
	public EffectEnabledType chunkingEnabled;
	
	@Desc("Some printouts for when shaders bug out")
	public TestState shaderDebug;
	
	@Desc("Whether or not to use shaders")
	public ShaderEnabledState enabled;

}
