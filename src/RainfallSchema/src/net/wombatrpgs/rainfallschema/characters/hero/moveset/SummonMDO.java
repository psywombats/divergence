/**
 *  SummonSchema.java
 *  Created on Dec 28, 2012 12:18:34 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.EmitterMDO;
import net.wombatrpgs.rainfallschema.graphics.GibsetMDO;

/**
 * Summons the bloock.
 */
@Path("characters/hero/moveset/")
public class SummonMDO extends MoveMDO {
	
	@Desc("Duration - how long it takes to summon the block, in seconds")
	public Float duration;
	
	@Desc("Block event - which event to use for the block")
	@SchemaLink(CharacterEventMDO.class)
	public String blockEvent;
	
	@Desc("Block animation - plays as the block phases into existance")
	@SchemaLink(AnimationMDO.class)
	public String blockAnimation;
	
	@Desc("Fail animation - plays as block kills itself during summoning")
	@SchemaLink(AnimationMDO.class)
	public String failAnimation;
	
	@Desc("Gibsets - the partciles of the block when it kills itself")
	@SchemaLink(GibsetMDO.class)
	@Nullable
	public String gibs;
	
	@Desc("Emitter - emits the block particles")
	@SchemaLink(EmitterMDO.class)
	@Nullable
	public String emitter;

}
