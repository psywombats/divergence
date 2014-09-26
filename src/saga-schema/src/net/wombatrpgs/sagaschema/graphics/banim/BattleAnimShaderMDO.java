/**
 *  BattleAnimShaderMDO.java
 *  Created on Jul 31, 2014 12:03:38 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics.banim;

import net.wombatrpgs.mgneschema.graphics.ShaderMDO;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleAnimMDO;
import net.wombatrpgs.sagaschema.graphics.banim.data.ShaderScopeType;

/**
 * Battle animation that operates by warping the enemy.
 */
@Path("graphics/")
public class BattleAnimShaderMDO extends BattleAnimMDO {
	
	@Desc("Shader - will apply to all enemies")
	@SchemaLink(ShaderMDO.class)
	public String shader;
	
	@Desc("Duration - in seconds")
	public Float duration;
	
	@Desc("Scope - will still apply to all enemies")
	@DefaultValue("ENEMY_AREA")
	public ShaderScopeType scope;

}
