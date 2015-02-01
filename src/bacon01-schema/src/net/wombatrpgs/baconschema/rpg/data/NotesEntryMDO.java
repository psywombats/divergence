/**
 *  NotesEntryMDO.java
 *  Created on Jan 31, 2015 2:10:28 PM for project bacon01-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.baconschema.rpg.data;

import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * notes set
 */
public class NotesEntryMDO extends HeadlessSchema {
	
	@FileLink("ui")
	public String graphic;

}
