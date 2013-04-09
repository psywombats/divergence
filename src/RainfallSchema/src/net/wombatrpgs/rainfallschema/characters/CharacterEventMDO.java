/**
 *  EventMDO.java
 *  Created on Nov 12, 2012 4:45:16 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.data.CollisionResponseType;
import net.wombatrpgs.rainfallschema.characters.data.HitboxType;
import net.wombatrpgs.rainfallschema.characters.enemies.data.TouchEffectType;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;

/**
 * An interactive component on the map is called an "Event." (it's an entity,
 * but let's pretend, okay?)
 */
@Path("characters/")
public class CharacterEventMDO extends MainSchema {
	
	@Desc("Walk animation - what this event looks like when moving")
	@SchemaLink(DirMDO.class)
	@Nullable
	public String appearance;
	
	@Desc("Idle animation - what this event looks like when still, (if nothing, will use first frame of walk animation)")
	@SchemaLink(DirMDO.class)
	@Nullable
	public String idleAnim;
	
	@Desc("Hitbox type - what shape of hitbox this character has")
	@DefaultValue("NONE")
	public HitboxType collision;
	
	@Desc("Collision response - what happens when the hero runs into us")
	@DefaultValue("ETHEREAL")
	public CollisionResponseType response;
	
	@Desc("Touch effect - describes what happens when the chara hits hero")
	@DefaultValue("NOTHING")
	public TouchEffectType touch;
	
	@Desc("Hurt sound - plays when this character is damaged")
	@SchemaLink(SoundMDO.class)
	@Nullable
	public String soundHurt;
	
	@Desc("Mobility - speed and acceleration parameters")
	@SchemaLink(MobilityMDO.class)
	public String mobility;

}
