/**
 *  RecruitSelectionMDO.java
 *  Created on Jun 20, 2014 9:56:44 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * List of characters that can be recruited from a specific menu.
 */
@Path("rpg/")
public class RecruitSelectionMDO extends MainSchema {
	
	@Desc("The title of the recruit text")
	public String title;
	
	@Desc("Recruitable characters")
	@SchemaLink(CharaMDO.class)
	public String[] options;

}
