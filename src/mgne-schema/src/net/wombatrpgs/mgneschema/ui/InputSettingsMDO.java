/**
 *  InputSettingsMDO.java
 *  Created on Jan 22, 2014 12:27:40 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.ui;

import net.wombatrpgs.mgneschema.io.KeymapMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Stuff for default input.
 */
@Path("settings/")
public class InputSettingsMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;
	@Desc("Default keymap")
	@SchemaLink(KeymapMDO.class)
	public String keymap;

}
