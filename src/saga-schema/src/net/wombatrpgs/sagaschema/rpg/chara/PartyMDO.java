/**
 *  PartyMDO.java
 *  Created on Apr 2, 2014 10:04:13 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.sagaschema.rpg.chara.data.PartyEntryMDO;

/**
 * So a bunch of monsters walk into a bar...
 */
@Path("rpg/")
public class PartyMDO extends MainSchema {
	
	@Desc("Party members")
	@InlineSchema(PartyEntryMDO.class)
	public PartyEntryMDO members[];

}
