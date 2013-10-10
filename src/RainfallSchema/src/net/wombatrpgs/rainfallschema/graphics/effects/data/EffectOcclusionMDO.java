/**
 *  EffectOcclusionMDO.java
 *  Created on Oct 9, 2013 1:41:10 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.graphics.effects.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.ShaderMDO;

/**
 * An effect that doesn't show up on impassable tiles.
 */
@Path("graphics/effects/")
public class EffectOcclusionMDO extends EffectMDO {
	
	@Desc("Animation that plays on all tiles")
	@SchemaLink(AnimationMDO.class)
	public String allAnim;
	
	@Desc("Animation that plays only on passable tiles")
	@SchemaLink(AnimationMDO.class)
	public String groundAnim;
	
	@Desc("Shader for global effect")
	@SchemaLink(ShaderMDO.class)
	public String allShader;
	
	@Desc("Shader for passable tiles")
	@SchemaLink(ShaderMDO.class)
	public String groundShader;

}
