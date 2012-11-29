/**
 *  FourDirMDO.java
 *  Created on Nov 12, 2012 5:16:02 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Four animations combined into directions. Usually used for characters'
 * appearance.
 */
@Path("graphics/")
public class FourDirMDO extends MainSchema {
	
	@Desc("Upwards-facing animation")
	@SchemaLink(AnimationMDO.class)
	public String upAnim;
	
	@Desc("Right-facing animation")
	@SchemaLink(AnimationMDO.class)
	public String rightAnim;
	
	@Desc("Downwards-facing animation")
	@SchemaLink(AnimationMDO.class)
	public String downAnim;
	
	@Desc("Left-facing animation")
	@SchemaLink(AnimationMDO.class)
	public String leftAnim;
	
	@Desc("Excess height - the pixels at the top of the sprite that can be " +
			"walked under (in pixels)")
	@DefaultValue("0")
	public Integer excessHeight;

}
