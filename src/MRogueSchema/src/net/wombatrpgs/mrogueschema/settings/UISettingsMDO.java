/**
 *  UISettingsMDO.java
 *  Created on Feb 2, 2013 3:51:54 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.IconSetMDO;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.HudMDO;
import net.wombatrpgs.mrogueschema.ui.InventoryMenuMDO;
import net.wombatrpgs.mrogueschema.ui.NarratorMDO;
import net.wombatrpgs.mrogueschema.ui.PromptMDO;
import net.wombatrpgs.mrogueschema.ui.SkillsBoxMDO;
import net.wombatrpgs.mrogueschema.ui.TextBoxMDO;

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
	
	@Desc("Default HUD")
	@SchemaLink(HudMDO.class)
	@Nullable
	public String hud;
	
	@Desc("Default skills HUD")
	@SchemaLink(SkillsBoxMDO.class)
	@Nullable
	public String skills;
	
	@Desc("Default icon set")
	@SchemaLink(IconSetMDO.class)
	public String icons;
	
	@Desc("Default narrator")
	@SchemaLink(NarratorMDO.class)
	public String narrator;
	
	@Desc("Default inventory")
	@SchemaLink(InventoryMenuMDO.class)
	public String inventory;
	
	@Desc("Exit game prompt")
	@SchemaLink(PromptMDO.class)
	public String prompt;
	
	@Desc("Cursor graphic")
	@SchemaLink(AnimationMDO.class)
	public String cursor;

}
