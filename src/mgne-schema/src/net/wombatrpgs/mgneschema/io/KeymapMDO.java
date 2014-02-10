/**
 *  KeymapMDO.java
 *  Created on Jan 21, 2014 11:26:13 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.io;

import net.wombatrpgs.mgneschema.io.data.KeyButtonPairMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A mapping from keys on the keyboard to in-game commands. In-game commands
 * are things like A-button B-button on gameboy etc that could have different
 * contexts in different areas.
 */
@Path("io/")
public class KeymapMDO extends MainSchema {
	
	@Desc("Keybindings")
	@InlineSchema(KeyButtonPairMDO.class)
	public KeyButtonPairMDO[] bindings;

}
