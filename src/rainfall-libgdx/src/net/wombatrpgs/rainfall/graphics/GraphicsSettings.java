/**
 *  GraphicsSettings.java
 *  Created on Sep 3, 2013 5:43:35 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.data.EffectEnabledType;
import net.wombatrpgs.rainfallschema.settings.GraphicsSettingsMDO;

/**
 * Settings for graphics? These probably shouldn't get changed much in-game,
 * unless it's from some options menu that's yet to be implemented. Also
 * contains some global helper methods, like shadow rendering.
 */
public class GraphicsSettings implements Queueable {
	
	protected GraphicsSettingsMDO mdo;
	protected AnimationStrip shadow;
	
	/**
	 * Creates a new graphics settings from data. Should only be called once per
	 * game lifetime.
	 * @param	mdo				The data to load settings from
	 */
	public GraphicsSettings(GraphicsSettingsMDO mdo) {
		this.mdo = mdo;
		this.shadow = new AnimationStrip(RGlobal.data.getEntryFor(mdo.shadowSprite, AnimationMDO.class));
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		shadow.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		shadow.postProcessing(manager, pass);
	}
	
	/**
	 * Retrieves the global shadow object shown beneath characters. Although the
	 * type is AnimationStrip, chances are it's just a single frame.
	 * @return					The shadow sprite
	 */
	public AnimationStrip getShadow() {
		return shadow;
	}
	
	/**
	 * Retrieves the shadow fudge factor.
	 * @return					The shadow offset y, in px
	 */
	public int getShadowY() {
		return mdo.shadowY;
	}
	
	/**
	 * Draws a shadow at the specified location. Right now it doesn't chunk
	 * properly. The passed coordinates should be the anchor to start drawing
	 * at, rather than the center. CharacterEvent already has code to calculate
	 * this, even though it probably shouldn't.
	 * @param	event			The event to render the shadow under
	 * @param	camera			The camera to render with
	 * @param	x				The x-coord to center the shadow at
	 * @param	y				The y-coord to center the shadow at
	 */
	public void drawShadow(CharacterEvent event, OrthographicCamera camera, int x, int y) {
		TextureRegion tex = RGlobal.graphics.getShadow().getRegion();
		int renderX = ((int) Math.floor(event.getX() + x)) % 32;
		int renderY = ((int) Math.floor(event.getY() + y - getShadowY())) % 32;
		int origWidth = tex.getRegionWidth();
		int origHeight = tex.getRegionHeight();
		int width = 0;
		int height = 0;
		for (int atY = 0; atY < origHeight; atY += height) {
			height = 32 - ((atY+renderY) % 32);
			if (height < 0) height += 32;
			if (atY + height >= origHeight) height = origHeight;
			for (int atX = 0; atX < origWidth; atX += width) {
				width = 32 - ((atX+renderX) % 32);
				if (width < 0) width += 32;
				if (atX + width > origWidth) width = origWidth - atX;
				tex.setRegion(atX, origHeight-atY-height, width, height);
				int tileX = (int) Math.floor((atX + x + event.getX()) / 32);
				int tileY = (int) Math.floor((atY + y + event.getY()) / 32);
				if (event.getLevel().tileExistsAt(tileX, tileY, event.getZ())) {
					event.renderLocal(camera, tex, atX+x, atY+y-getShadowY(), 0);
				}
			}
		}
		tex.setRegion(0, 0, origWidth, origHeight);
	}
	
	/**
	 * Performs the chunking operation that was previously in the event layer;
	 * it should have a better home here. Chunking ensures that a character
	 * appears in front of and behind the tiles it should based on height. Our
	 * high-level strategy is to split the sprite into 32-height chunks aligned
	 * with the map and render each separately.
	 * @param 	event			The event being rendered
	 * @param 	camera			The camera being used to render
	 * @param 	z				The z-layer currently being rendered
	 * @param	layerZ			The z-layer of the eventlayer calling us
	 */
	public void chunkEvent(MapEvent event, OrthographicCamera camera, float z, float layerZ) {
		if (event.requiresChunking() && mdo.chunkingEnabled == EffectEnabledType.ENABLED) {
			TextureRegion region = event.getRegion();
			int deltaZ = (int) (z - event.getZ());
			int maxHeight = (int) Math.ceil(region.getRegionHeight() / 32.f);
			if (deltaZ > maxHeight) {
				return;
			}
			if (deltaZ == 0) {
				event.finishChunking(camera);
			}
			int gap = (int) (Math.floor(event.getY())) % 32;
			if (event.getY()+1 < 0) {
				gap *= -1;
			}
			int botY = region.getRegionHeight() - (deltaZ) * 32 + gap;
			int topY = region.getRegionHeight() - (deltaZ+1) * 32 + gap;
			if (botY > region.getRegionHeight()) botY = region.getRegionHeight();
			if (topY < 0) topY = 0;
			if (botY < 0) return;
			TextureRegion chunk = new TextureRegion(region,
					0, topY,
					region.getRegionWidth(), botY - topY);
			event.renderLocal(camera, chunk, 0, region.getRegionHeight() - botY, 0);
		} else if ((int) Math.floor(layerZ) == z) {
			event.render(camera);
		}
	}

}
