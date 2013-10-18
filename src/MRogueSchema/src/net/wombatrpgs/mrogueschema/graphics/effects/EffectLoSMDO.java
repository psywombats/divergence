/**
 *  EffectLoS.java
 *  Created on Oct 6, 2013 5:55:03 PM for project MRogueSchema
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
 * LoS obscuring effect.
 */
@Path("graphics/effects/")
public class EffectLoSMDO extends EffectMDO {
	
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
