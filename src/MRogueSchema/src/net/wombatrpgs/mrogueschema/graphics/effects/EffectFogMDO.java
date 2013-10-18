/**
 *  EffectFogMDO.java
 *  Created on Apr 18, 2013 10:18:48 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.EffectMDO;

/**
 * Fogs up the beginning portions of the game.
 */
@Path("graphics/effects/")
public class EffectFogMDO extends EffectMDO {
	
	@Desc("Fog shader")
	@SchemaLink(ShaderMDO.class)
	public String shader;
	
	@Desc("Fog texture/anim")
	@SchemaLink(AnimationMDO.class)
	public String tex;
	
	@Desc("Fog velocity - in px/s")
	public Integer velocity;
	
	@Desc("Fog density - 1 highest, 0 invisble")
	public Float density;
	
	@Desc("Beacon radius - how far out the beacons reach, in pixels")
	public Integer radius;

	@Desc("Beacon factor - the max percent beacons can light up fog, 1 all the way 0 not a t all")
	public Float factor;
	
	@Desc("Beacon decay - exponential factor for beacon distance, usually from 1 to 3, 1 giving " +
			"linear illumination from center and 3 giving cubic")
	public Float exponent;
}
