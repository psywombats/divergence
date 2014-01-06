/**
 *  UISettingsMDO.java
 *  Created on Feb 2, 2013 3:51:54 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.graphics.AnimationMDO;
import net.wombatrpgs.sagaschema.graphics.IconSetMDO;
import net.wombatrpgs.sagaschema.ui.FontMDO;
import net.wombatrpgs.sagaschema.ui.NarratorMDO;
import net.wombatrpgs.sagaschema.ui.TextBoxMDO;

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
	
	@Desc("Default narrator")
	@SchemaLink(NarratorMDO.class)
	public String narrator;
	
	@Desc("Cursor graphic")
	@SchemaLink(AnimationMDO.class)
	public String cursor;

}
