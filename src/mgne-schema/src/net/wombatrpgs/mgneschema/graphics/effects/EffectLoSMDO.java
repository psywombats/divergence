/**
 *  EffectLoS.java
 *  Created on Oct 6, 2013 5:55:03 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.graphics.effects;

import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;
import net.wombatrpgs.mgneschema.graphics.effects.data.EffectMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * LoS obscuring effect.
 */
@Path("graphics/effects/")
public class EffectLoSMDO extends EffectMDO {
	
	private static final long serialVersionUID = 1L;

	@Desc("Shader")
	@SchemaLink(ShaderMDO.class)
	public String shader;
	
	@Desc("Unseen texture")
	@SchemaLink(AnimationMDO.class)
	public String invisibleTex;
	
	@Desc("Never seen texture")
	@SchemaLink(AnimationMDO.class)
	public String unseenTex;
	
	@Desc("Fog velocity - in px/s")
	public Integer velocity;

}
