/**
 *  UISettings.java
 *  Created on Feb 4, 2013 5:07:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.AnimationStrip;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBox;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.IconSetMDO;
import net.wombatrpgs.mrogueschema.settings.UISettingsMDO;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.HudMDO;
import net.wombatrpgs.mrogueschema.ui.InventoryMenuMDO;
import net.wombatrpgs.mrogueschema.ui.NarratorMDO;
import net.wombatrpgs.mrogueschema.ui.PromptMDO;
import net.wombatrpgs.mrogueschema.ui.SkillsBoxMDO;
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
	protected SkillsBox skills;
	protected IconSet icons;
	protected Narrator narrator;
	protected InventoryMenu inventory;
	protected AnimationStrip cursor;
	protected Prompt quitPrompt;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new UI settings using MDO data for defaults.
	 * @param 	mdo				The data to make object from
	 */
	public UISettings(UISettingsMDO mdo) {
		this.mdo = mdo;
		this.assets = new ArrayList<Queueable>();
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		assets.add(font);
		box = new TextBox(MGlobal.data.getEntryFor(mdo.box, TextBoxMDO.class), font);
		assets.add(box);
		if (MapThing.mdoHasProperty(mdo.hud)) {
			hud = new Hud(MGlobal.data.getEntryFor(mdo.hud, HudMDO.class));
			assets.add(hud);
		}
		if (MapThing.mdoHasProperty(mdo.skills)) {
			skills = new SkillsBox(MGlobal.data.getEntryFor(mdo.skills, SkillsBoxMDO.class));
			assets.add(skills);
		}
		icons = new IconSet(MGlobal.data.getEntryFor(mdo.icons, IconSetMDO.class));
		assets.add(icons);
		narrator = new Narrator(MGlobal.data.getEntryFor(mdo.narrator, NarratorMDO.class), font);
		assets.add(narrator);
		inventory = new InventoryMenu(MGlobal.data.getEntryFor(mdo.inventory, InventoryMenuMDO.class));
		assets.add(inventory);
		cursor = new AnimationStrip(MGlobal.data.getEntryFor(mdo.cursor, AnimationMDO.class));
		assets.add(cursor);
		quitPrompt = new Prompt(MGlobal.data.getEntryFor(mdo.prompt, PromptMDO.class));
		assets.add(quitPrompt);
		
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}
	
	/** @return The text box associated with these settings */
	public TextBox getBox() { return this.box; }
	
	/** @return The font associated with these settings */
	public FontHolder getFont() { return this.font; }
	
	/** return The narrator associated with these settings */
	public Narrator getNarrator() { return this.narrator; }
	
	/** @return The skills associated with these settings */
	public SkillsBox getSkills() { return this.skills; }
	
	/** @return The HUD associated with these settings */
	public Hud getHud() { return this.hud; }
	
	/** @return The inventory menu associated with these settings */
	public InventoryMenu getInventory() { return this.inventory; }
	
	/** @return The animation to use for the map cursor */
	public AnimationStrip getCursor() { return this.cursor; }
	
	/** @return The prompt for quit game */
	public Prompt getPrompt() { return this.quitPrompt; }

}
