/**
 *  DoorSetMDO.java
 *  Created on Oct 25, 2013 3:34:41 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.graphics.DirMDO;

/**
 * A door for a dungeon wow.
 */
@Path("maps/")
public class DoorSetMDO extends MainSchema {
	
	@Desc("Door closed appearance")
	@SchemaLink(DirMDO.class)
	public String closeAnim;
	
	@Desc("Door open appearance")
	@SchemaLink(DirMDO.class)
	public String openAnim;

}
