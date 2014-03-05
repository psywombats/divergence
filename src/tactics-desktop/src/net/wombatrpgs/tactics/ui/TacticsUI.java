/**
 *  TacticsUI.java
 *  Created on Feb 12, 2014 11:29:49 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.tacticsschema.settings.TacticsUISettingsMDO;
import net.wombatrpgs.tacticsschema.ui.CursorMDO;
import net.wombatrpgs.tacticsschema.ui.DirectionSelectorMDO;

/**
 * Singleton to hold a bunch of tactics ui objects.
 */
public class TacticsUI implements Queueable {
	
	protected static final String KEY_DEFAULT_MDO = "tactics_ui_default";
	
	protected TacticsUISettingsMDO mdo;
	protected List<Queueable> assets;
	
	protected Graphic highlight;
	protected DirectionSelector target;
	protected MapCursor cursor;
	
	/**
	 * Creates a new tactics ui. I can't imagine what would call this besides
	 * TGlobal.
	 * @param	mdo				The data to load from
	 */
	protected TacticsUI(TacticsUISettingsMDO mdo) {
		this.mdo = mdo;
		assets = new ArrayList<Queueable>();
		
		highlight = new Graphic(mdo.mapHighlight);
		target = new DirectionSelector(MGlobal.data.getEntryFor(mdo.target, DirectionSelectorMDO.class));
		cursor = new MapCursor(MGlobal.data.getEntryFor(mdo.cursor, CursorMDO.class));
		assets.add(highlight);
		assets.add(cursor);
		assets.add(target);
	}
	
	/**
	 * Initializes the singleton based on the default UI.
	 * @return					The single tactics ui object
	 */
	public static TacticsUI init() {
		return new TacticsUI(MGlobal.data.getEntryFor(KEY_DEFAULT_MDO, TacticsUISettingsMDO.class));
	}
	
	/** @return The highlight for walkable map squares */
	public Graphic getHighlight()  { return highlight; }
	
	/** @return The cursor for map selection */
	public MapCursor getCursor() { return cursor; }
	
	/** @return The target for direction input (default) */
	public DirectionSelector getDirectionSelector() { return target; }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

}
