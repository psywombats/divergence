/**
 *  NotesSet.java
 *  Created on Jan 31, 2015 2:09:45 PM for project bacon01-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.baconschema.rpg;

import net.wombatrpgs.baconschema.rpg.data.NotesEntryMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A collection of notes graphics
 */
@Path("rpg/")
public class NotesSetMDO extends MainSchema {
	
	@Desc("Notes graphics")
	@InlineSchema(NotesEntryMDO.class)
	public NotesEntryMDO[] notes;

}
