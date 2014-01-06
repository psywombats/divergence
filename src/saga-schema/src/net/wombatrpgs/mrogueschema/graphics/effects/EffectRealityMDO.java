/**
 *  FxTextMDO.java
 *  Created on Oct 18, 2013 6:59:40 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.EffectMDO;

/**
 * One of the weirdest graphical effects of all time. Don't make one of these
 * jesus christ.
 */
@Path("graphics/effects/")
public class EffectRealityMDO extends EffectMDO {
	
	@Desc("Shader")
	@SchemaLink(ShaderMDO.class)
	public String shader;
	
	@Desc("Texture to use for alpha map")
	@FileLink("textures")
	public String graphic;

}
