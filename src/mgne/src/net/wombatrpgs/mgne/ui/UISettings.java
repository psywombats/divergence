/**
 *  UISettings.java
 *  Created on Feb 4, 2013 5:07:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBox;
import net.wombatrpgs.mgneschema.graphics.IconSetMDO;
import net.wombatrpgs.mgneschema.settings.UISettingsMDO;
import net.wombatrpgs.mgneschema.ui.FontMDO;
import net.wombatrpgs.mgneschema.ui.NinesliceMDO;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;

/**
 * Holds current settings for cutscene UI and other HUD things. These things
 * are considered global, so short of copying around a UISettings object and
 * pasting it into RGlobal, changes here will affect everywhere. In fact, these
 * things are final because changing them would involve re-calling the asset
 * queue on this object.
 */
public class UISettings extends AssetQueuer {
	
	public static String DEFAULT_MDO_KEY = "default_ui";
	
	protected UISettingsMDO mdo; // this is only the original default settings
	protected FontHolder font;
	protected TextBox box;
	protected IconSet icons;
	protected NinesliceMDO ninesliceMDO;
	protected Graphic cursor;
	
	/**
	 * Creates a new UI settings using MDO data for defaults.
	 * @param 	mdo				The data to make object from
	 */
	public UISettings(UISettingsMDO mdo) {
		super();
		this.mdo = mdo;
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		assets.add(font);
		box = new TextBox(MGlobal.data.getEntryFor(mdo.box, TextBoxMDO.class), font);
		assets.add(box);
		icons = new IconSet(MGlobal.data.getEntryFor(mdo.icons, IconSetMDO.class));
		assets.add(icons);
		cursor = new Graphic(mdo.cursor);
		assets.add(cursor);
		
		ninesliceMDO = MGlobal.data.getEntryFor(mdo.nineslice, NinesliceMDO.class);
	}
	
	/** @return The text box associated with these settings */
	public TextBox getBox() { return this.box; }
	
	/** @return The font associated with these settings */
	public FontHolder getFont() { return this.font; }
	
	/** @return The nineslice data, instance and queue yourself */
	public NinesliceMDO getNinesliceMDO() { return this.ninesliceMDO; }
	
	/** @return The default cursor graphic */
	public Graphic getCursor() { return this.cursor; }

}
