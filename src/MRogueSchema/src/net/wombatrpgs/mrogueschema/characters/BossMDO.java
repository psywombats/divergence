/**
 *  BossMDO.java
 *  Created on Oct 27, 2013 3:29:42 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.AbilFxMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.EffectMDO;

/**
 * Endgame boss.
 */
@Path("characters/")
public class BossMDO extends EnemyMDO {
	
	@Desc("Scene that plays when boss is sighted")
	@SchemaLink(SceneParentMDO.class)
	public String sightedScene;
	
	@Desc("Ambient graphical effect")
	@SchemaLink(EffectMDO.class)
	@Nullable
	public String effect;
	
	@Desc("Spawnstep abilfx")
	@SchemaLink(AbilFxMDO.class)
	public String abilFX;

}
