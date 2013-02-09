/**
 *  Hud.java
 *  Created on Feb 6, 2013 1:56:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.Level;
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
public class Hud extends Picture {
	
	protected HudMDO mdo;
	protected Graphic mask, alphaMask;
	protected boolean enabled;

	/**
	 * Creates a new HUD from data. Requires queueing.
	 * @param 	mdo				The data to create from
	 */
	public Hud(HudMDO mdo) {
		super(RGlobal.data.getEntryFor(mdo.graphic, GraphicMDO.class), 0, 0, 50);
		this.mdo = mdo;
		mask = new Graphic(RGlobal.data.getEntryFor(mdo.mask, GraphicMDO.class));
		alphaMask = new Graphic(RGlobal.data.getEntryFor(mdo.alphaMask, GraphicMDO.class));
	}
	
	/** @return True if the hud is displaying right now */
	public boolean isEnabled() { return enabled; }
	
	/** @param True if the hud is going to be displayed */
	public void setEnabled(boolean enabled) { this.enabled = enabled; }

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
		Graphic minimap = RGlobal.hero.getLevel().getMinimap();

		if (minimap != null) {
			int x1 = (int) (x + mdo.minimapX);
			int y1 = (int) (y + appearance.getHeight() - mask.getHeight() - mdo.minimapY);
			int minX = Integer.valueOf(RGlobal.hero.getLevel().getProperty(
					Level.PROPERTY_MINIMAP_X1));
			int minY = Integer.valueOf(RGlobal.hero.getLevel().getProperty(
					Level.PROPERTY_MINIMAP_Y1));
			int maxX = Integer.valueOf(RGlobal.hero.getLevel().getProperty(
					Level.PROPERTY_MINIMAP_X2));
			int maxY = Integer.valueOf(RGlobal.hero.getLevel().getProperty(
					Level.PROPERTY_MINIMAP_Y2));
			int offX = (int) (minX + ((float) RGlobal.hero.getX() / 
					(float) RGlobal.hero.getLevel().getWidthPixels()) * 
					(maxX - minX));
			int offY = (int)(minY - ((float) RGlobal.hero.getY() / 
					(float) RGlobal.hero.getLevel().getHeightPixels()) * 
					(maxY - minY) + (maxY - minY));
			getBatch().end();
			getBatch().setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
			Gdx.graphics.getGL20().glColorMask(false, false, false, true);
			getBatch().begin();
			alphaMask.renderAt(getBatch(), x1, y1);
			getBatch().end();
			getBatch().setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_DST_ALPHA);
			Gdx.graphics.getGL20().glColorMask(true, true, true, true);
			getBatch().begin();
			getBatch().draw(minimap.getTexture(),
					x1,
					y1,
					-mask.getWidth()/2 + offX, 
					-mask.getHeight()/2 + offY,
					mask.getWidth(),
					mask.getHeight());
			getBatch().end();
			getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			getBatch().begin();
			mask.renderAt(getBatch(), x1, y1);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.objects.Picture#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		if (alphaMask != null) {
			alphaMask.queueRequiredAssets(manager);
		}
		if (mask != null) {
			mask.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.objects.Picture#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (mask != null) {
			mask.postProcessing(manager, pass);
		}
		if (alphaMask != null) {
			alphaMask.postProcessing(manager, pass);
		}
		switch (mdo.anchorDir) {
		case DOWN:
			x = (RGlobal.window.width - appearance.getWidth()) / 2;
			y = 0;
			break;
		case UP:
			x = (RGlobal.window.width - appearance.getWidth()) / 2;
			y = RGlobal.window.height - appearance.getHeight();
			break;
		case LEFT:
			x = 0;
			y = (RGlobal.window.height - appearance.getHeight()) / 2;
			break;
		case RIGHT:
			x = RGlobal.window.width - appearance.getWidth();
			y = (RGlobal.window.height - appearance.getHeight()) / 2;
			break;
		}
	}

}
