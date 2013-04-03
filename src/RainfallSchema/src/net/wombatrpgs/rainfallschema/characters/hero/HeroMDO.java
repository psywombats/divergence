/**
 *  HeroMDO.java
 *  Created on Jan 26, 2013 6:14:09 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;

/**
 * Defines stuff about the hero and how it moves/acts.
 */
@Path("characters/hero/")
public class HeroMDO extends CharacterEventMDO {
	
	@Desc("Death sfx - plays when the hero dies")
	@SchemaLink(SoundMDO.class)
	public String deathSound;

}
