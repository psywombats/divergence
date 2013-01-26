/**
 *  EventMDO.java
 *  Created on Nov 12, 2012 4:45:16 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;
import net.wombatrpgs.rainfallschema.maps.data.CollisionResponseType;
import net.wombatrpgs.rainfallschema.maps.data.HitboxType;

/**
 * An interactive component on the map is called an "Event." (it's an entity,
 * but let's pretend, okay?)
 */
@Path("maps/")
public class CharacterEventMDO extends MainSchema {
	
	@Desc("Appearance - what this event looks like")
	@SchemaLink(DirMDO.class)
	@Nullable
	public String appearance;
	
	@Desc("Hitbox type - what shape of hitbox this character has")
	@DefaultValue("NONE")
	public HitboxType collision;
	
	@Desc("Collision response - what happens when the hero runs into us")
	@DefaultValue("ETHEREAL")
	public CollisionResponseType response;

}
