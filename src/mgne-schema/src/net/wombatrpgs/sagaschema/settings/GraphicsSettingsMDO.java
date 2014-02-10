/**
 *  GraphicsSettingsMDO.java
 *  Created on Sep 3, 2013 5:28:49 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.graphics.data.EffectEnabledType;
import net.wombatrpgs.sagaschema.settings.data.ShaderEnabledState;
import net.wombatrpgs.sagaschema.test.data.TestState;

/**
 * Controls some global graphics configurationy things.
 */
@Path("graphics/")
public class GraphicsSettingsMDO extends MainSchema {
	
	@Desc("Chunking - (advanced) enables/disables the character order rendering algorithm")
	public EffectEnabledType chunkingEnabled;
	
	@Desc("Some printouts for when shaders bug out")
	public TestState shaderDebug;
	
	@Desc("Whether or not to use shaders")
	public ShaderEnabledState enabled;

}
