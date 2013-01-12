/**
 *  SummonSchema.java
 *  Created on Dec 28, 2012 12:18:34 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.hero.moveset;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * Summons the bloock.
 */
@Path("hero/moveset/")
public class SummonMDO extends MoveMDO {
	
	@Desc("Duration -- how long it takes to summon the block, in seconds")
	public Float duration;
	
	@Desc("Block event -- which event to use for the block")
	@SchemaLink(CharacterEventMDO.class)
	public String blockEvent;

}
