/**
 *  AbilFxFlamesMDO.java
 *  Created on Oct 18, 2013 4:43:07 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.effects;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.AbilFxMDO;

/**
 * PK FIYAH
 */
@Path("graphics/effects/")
public class AbilFxFlamesMDO extends AbilFxMDO {
	
	@Desc("Shader")
	@SchemaLink(ShaderMDO.class)
	public String shader;
	
	@Desc("Texture to use for circular mask")
	@FileLink("textures")
	public String mask;
	
	@Desc("Texture to use for flame map")
	@FileLink("textures")
	public String flames;
	
	@Desc("Texture to use for perturbrance 1")
	@FileLink("textures")
	public String noise1;
	
	@Desc("Texture to use for perturbrance 2")
	@FileLink("textures")
	public String noise2;
	
	@Desc("Texture to use for perturbrance 3")
	@FileLink("textures")
	public String noise3;
	
	@Desc("Fadein/expand duration")
	public Float fadein;

}
