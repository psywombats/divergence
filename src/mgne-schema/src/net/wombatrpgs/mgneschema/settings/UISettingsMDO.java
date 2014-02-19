/**
 *  UISettingsMDO.java
 *  Created on Feb 2, 2013 3:51:54 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings;

import net.wombatrpgs.mgneschema.graphics.IconSetMDO;
import net.wombatrpgs.mgneschema.ui.FontMDO;
import net.wombatrpgs.mgneschema.ui.NinesliceMDO;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Some UI stuff?
 */
@Path("settings/")
public class UISettingsMDO extends MainSchema {
	
	@Desc("Default font")
	@SchemaLink(FontMDO.class)
	public String font;
	
	@Desc("Default text box")
	@SchemaLink(TextBoxMDO.class)
	public String box;
	
	@Desc("Default icon set")
	@SchemaLink(IconSetMDO.class)
	public String icons;
	
	@Desc("Default nineslice - used as the default for various minimenus")
	@SchemaLink(NinesliceMDO.class)
	public String nineslice;

}
