/**
 *  UISettings.java
 *  Created on Feb 4, 2013 5:07:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBox;
import net.wombatrpgs.mrogueschema.graphics.IconSetMDO;
import net.wombatrpgs.mrogueschema.settings.UISettingsMDO;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.HudMDO;
import net.wombatrpgs.mrogueschema.ui.NarratorMDO;
import net.wombatrpgs.mrogueschema.ui.TextBoxMDO;

/**
 * Holds current settings for cutscene UI and other HUD things. These things
 * are considered global, so short of copying around a UISettings object and
 * pasting it into RGlobal, changes here will affect everywhere. In fact, these
 * things are final because changing them would involve re-calling the asset
 * queue on this object.
 */
public class UISettings implements Queueable {
	
	public static String DEFAULT_MDO_KEY = "default_ui";
	
	protected UISettingsMDO mdo; // this is only the original default settings
	protected FontHolder font;
	protected TextBox box;
	protected Hud hud;
	protected IconSet icons;
	protected Narrator narrator;
	
	/**
	 * Creates a new UI settings using MDO data for defaults.
	 * @param 	mdo				The data to make object from
	 */
	public UISettings(UISettingsMDO mdo) {
		this.mdo = mdo;
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		box = new TextBox(MGlobal.data.getEntryFor(mdo.box, TextBoxMDO.class), font);
		if (!mdo.hud.equals(Constants.NULL_MDO)) {
			hud = new Hud(MGlobal.data.getEntryFor(mdo.hud, HudMDO.class));
		}
		icons = new IconSet(MGlobal.data.getEntryFor(mdo.icons, IconSetMDO.class));
		narrator = new Narrator(MGlobal.data.getEntryFor(mdo.narrator, NarratorMDO.class), font);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		font.queueRequiredAssets(manager);
		box.queueRequiredAssets(manager);
		if (hud != null) hud.queueRequiredAssets(manager);
		icons.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		font.postProcessing(manager, pass);
		box.postProcessing(manager, pass);
		if (hud != null) hud.postProcessing(manager, pass);
		icons.postProcessing(manager, pass);
	}
	
	/** @return The text box associated with these settings */
	public TextBox getBox() { return this.box; }
	
	/** @return The font associated with these settings */
	public FontHolder getFont() { return this.font; }
	
	/** return The narrator associated with these settings */
	public Narrator getNarrator() { return this.narrator; }
	
	/** @return The HUD associated with these settings */
	public Hud getHud() { return this.hud; }

}
