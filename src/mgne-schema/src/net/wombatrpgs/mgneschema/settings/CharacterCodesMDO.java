/**
 *  CharacterCodesMDO.java
 *  Created on Apr 12, 2014 3:57:26 AM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings;

import net.wombatrpgs.mgneschema.settings.data.SpecialCharacterMDO;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A list of special character codes.
 */
@Path("settings/")
public class CharacterCodesMDO extends MainSchema {
	
	@InlineSchema(SpecialCharacterMDO.class)
	public SpecialCharacterMDO[] codes;

}
