/**
 *  ClassSettingsMDO.java
 *  Created on Oct 28, 2013 8:39:35 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.characters.ClassMDO;
import net.wombatrpgs.mrogueschema.ui.FontMDO;

/**
 * Settings for the classes screen mostly.
 */
@Path("settings/")
public class ClassSettingsMDO extends MainSchema {
	
	@Desc("BG - What the background of the class select screen looks like")
	@FileLink("ui")
	public String bg;
	
	@Desc("Font - used for class desc")
	@SchemaLink(FontMDO.class)
	public String font;
	
	@Desc("Cursor graphic")
	@FileLink("ui")
	public String cursor;
	
	@Desc("Overpowered class")
	@SchemaLink(ClassMDO.class)
	public String op;

}
