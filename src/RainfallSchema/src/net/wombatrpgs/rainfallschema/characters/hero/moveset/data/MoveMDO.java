/**
 *  DashSchema.java
 *  Created on Dec 12, 2012 4:39:45 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;

/**
 * Base class for all schema that form part of the moveset. Hopefully
 * inheritance works in this little setup. Shouldn't appear on the tree either,
 * assuming the setup's working.
 */
@ExcludeFromTree
public class MoveMDO extends MainSchema {
	
	@Desc("Cooldown time, in seconds")
	@DefaultValue("0")
	public Float cooldown;
	
	@Desc("Initial stamina cost, in SP")
	@DefaultValue("0")
	public Integer stamina;
	
	@Desc("Sustain stamina cost, in SP/s")
	@DefaultValue("0")
	public Float sustainStamina;
	
	@Desc("Mobility - should the hero be able to move while this move is active?")
	public MoveMobility mobility;
	
	@Desc("SFX - played when move is started")
	@SchemaLink(SoundMDO.class)
	@Nullable
	public String sound;
	
	@Desc("4Dir Idle Animation - played when chara is in action and still. None for no change.")
	@SchemaLink(FourDirMDO.class)
	@Nullable
	public String staticAnimation;
	
	@Desc("4Dir Moving Animation - played when chara is in action and moving. None for no change.")
	@SchemaLink(FourDirMDO.class)
	@Nullable
	public String movingAnimation;

}
