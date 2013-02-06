/**
 *  Hud.java
 *  Created on Feb 6, 2013 1:56:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui;

import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.objects.Picture;
import net.wombatrpgs.rainfall.moveset.MovesetAct;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;
import net.wombatrpgs.rainfallschema.ui.HudMDO;
import net.wombatrpgs.rainfallschema.ui.data.IconPlacementMDO;

/**
 * Heads-up display! Everybody's favorite piece of UI. This version is stuck
 * into a UI object and should be told to draw itself as part of a screen.
 */
// TODO: make this display with the screen and not the level
public class Hud extends Picture {
	
	protected HudMDO mdo;

	/**
	 * Creates a new HUD from data. Requires queueing.
	 * @param 	mdo				The data to create from
	 */
	public Hud(HudMDO mdo) {
		super(RGlobal.data.getEntryFor(mdo.graphic, GraphicMDO.class), 0, 0, 50);
		this.mdo = mdo;
	}

	/**
	 * Remember we have to render all those pesky icons as well.
	 * @see net.wombatrpgs.rainfall.maps.objects.Picture#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		Map<InputCommand, MovesetAct> moves = RGlobal.hero.getMoves().getMoves();
		for (IconPlacementMDO iconMDO : mdo.icons) {
			for (InputCommand command : moves.keySet()) {
				if (command == iconMDO.command) {
					Graphic icon = moves.get(command).getIcon();
					icon.renderAt(getBatch(),
							getX() + iconMDO.offX,
							getY() + (appearance.getHeight() - iconMDO.offY - icon.getHeight()));
				}
			}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.objects.Picture#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		switch (mdo.anchorDir) {
		case DOWN:
			x = (RGlobal.window.defaultWidth - appearance.getWidth()) / 2;
			y = 0;
			break;
		case UP:
			x = (RGlobal.window.defaultWidth - appearance.getWidth()) / 2;
			y = RGlobal.window.defaultHeight - appearance.getHeight();
			break;
		case LEFT:
			x = 0;
			y = (RGlobal.window.defaultHeight - appearance.getHeight()) / 2;
			break;
		case RIGHT:
			x = RGlobal.window.defaultWidth - appearance.getWidth();
			y = (RGlobal.window.defaultHeight - appearance.getHeight()) / 2;
			break;
		}
	}

}
