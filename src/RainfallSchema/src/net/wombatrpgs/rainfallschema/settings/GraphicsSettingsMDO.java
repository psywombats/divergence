/**
 *  GraphicsSettingsMDO.java
 *  Created on Sep 3, 2013 5:28:49 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.data.EffectEnabledType;

/**
 * Controls some global graphics configurationy things.
 */
@Path("graphics/")
public class GraphicsSettingsMDO extends MainSchema {
	
	@Desc("Shadow sprite - Thingy sprite shown beneath characters, or none for"
			+ " no shadow")
	@SchemaLink(AnimationMDO.class)
	@Nullable
	public String shadowSprite;
	
	@Desc("Shadow offset Y - Fudge factor, higher values force shadows lower, "
			+ "in px")
	public Integer shadowY;
	
	@Desc("Chunking - (advanced) enables/disables the character order rendering algorithm")
	public EffectEnabledType chunkingEnabled;

}
