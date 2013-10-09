/**
 *  EffectPixelWeatherMDO.java
 *  Created on Oct 9, 2013 3:37:05 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.graphics.effects.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;

/**
 * Any sort of pixely tiling weather animation.
 */
@Path("graphics/effects/")
public class EffectPixelWeatherMDO extends EffectMDO {
	
	@Desc("Weather texture/anim")
	@SchemaLink(AnimationMDO.class)
	public String tex;

}
