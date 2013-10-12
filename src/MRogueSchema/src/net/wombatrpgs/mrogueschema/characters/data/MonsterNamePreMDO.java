/**
 *  MonsterNameSufMDO.java
 *  Created on Oct 12, 2013 3:00:51 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * Postfix for a monster name.
 */
@Path("characters/")
public class MonsterNamePreMDO extends HeadlessSchema {
	
	@Desc("Prefix")
	public String prefix;

}
