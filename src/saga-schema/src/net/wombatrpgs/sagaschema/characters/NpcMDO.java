/**
 *  NpcMDO.java
 *  Created on Jan 22, 2014 10:01:57 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.characters;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.sagaschema.characters.data.CharacterMDO;

/**
 * Dudes who hang around in pubs and say one-liners.
 */
@Path("charas/")
public class NpcMDO extends CharacterMDO {
	
	@Desc("Dialog - the dialog this NPC will chirp out, or empty for nothing")
	public String dialog;

}
