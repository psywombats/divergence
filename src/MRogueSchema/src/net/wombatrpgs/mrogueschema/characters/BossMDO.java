/**
 *  BossMDO.java
 *  Created on Oct 27, 2013 3:29:42 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.audio.MusicMDO;
import net.wombatrpgs.mrogueschema.characters.data.BossPicMDO;
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
	
	@Desc("Music that plays on cut end")
	@SchemaLink(MusicMDO.class)
	public String music;
	
	@Desc("Ambient graphical effect")
	@SchemaLink(EffectMDO.class)
	@Nullable
	public String effect;
	
	@Desc("Spawnstep abilfx")
	@SchemaLink(AbilFxMDO.class)
	public String abilFX;
	
	@Desc("Critical health scene")
	@SchemaLink(SceneParentMDO.class)
	public String deathScene;
	
	@Desc("Critical health spam ability")
	@SchemaLink(AbilityMDO.class)
	public String raveAbility;
	
	@Desc("Critical health music")
	@SchemaLink(MusicMDO.class)
	public String glitchMusic;
	
	@Desc("Critical BSOD")
	@FileLink("ui")
	public String bsod;
	
	@Desc("Critical troll message")
	@FileLink("ui")
	public String bsodMessage;
	
	@Desc("Scene that plays when boss is dead")
	@SchemaLink(SceneParentMDO.class)
	public String deadScene;
	
	@Desc("Game over screens")
	@InlineSchema(BossPicMDO.class)
	public BossPicMDO[] gos;

}
