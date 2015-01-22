/**
 *  BaconLevel.java
 *  Created on Jan 15, 2015 1:42:09 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;

import net.wombatrpgs.bacon01.EffectAltLight;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.AnimationStrip;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.maps.layers.GridLayer;
import net.wombatrpgs.mgne.maps.layers.LoadedGridLayer;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;

/**
 * Bacon game level, currently hacked for light testing.
 */
public class BaconLevel extends LoadedLevel {
	
	protected FrameBuffer lightBuffer, altBuffer;
	protected EffectAltLight effect;
	
	protected AnimationStrip light;
	float total = 0;

	public BaconLevel(LoadedMapMDO mdo, Screen screen) {
		super(mdo, screen);
		
		effect = new EffectAltLight(this);
		getScreen().addEffect(effect);
		assets.add(effect);
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		light.setScale(4);
		assets.add(light);
	}
	
	public FrameBuffer getLightBuffer() { return lightBuffer; }
	public FrameBuffer getAltBuffer() { return altBuffer; }
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
		lightBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (MapEvent event : getEventLayer().getAll()) {
			if (event == MGlobal.getHero()) continue;
			int screenX = event.getCenterX();
			int screenY = event.getCenterY();
//			screenX -= getScreen().getCamera().getTarget().getX();
//			screenY -= getScreen().getCamera().getTarget().getY();
			screenX -= getScreen().getWidth() / 2;
			screenY -= getScreen().getHeight() / 2;
			light.renderAt(getBatch(), screenX, screenY);
		}
		
		lightBuffer.end();
		
		altBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		altBuffer.end();
		
		getScreen().resumeNormalBuffer();
		
		renderGrid(batch, false);
		renderEvents(getScreen().getViewBatch());
		altBuffer.begin();
		renderEvents(getScreen().getViewBatch());
		altBuffer.end();
		getScreen().resumeNormalBuffer();
		renderGrid(batch, true);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		light.update(elapsed);
		total += elapsed;
		float scale = (float) (6f + Math.sin(total / 12f) * 4f);
		//light.setScale(scale);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#renderGrid
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, boolean)
	 */
	@Override
	protected void renderGrid(SpriteBatch batch, boolean upper) {
		for (GridLayer layer : gridLayers) {
			if (layer.getZ() < 1.f ^ upper) {
				LoadedGridLayer loadedLayer = (LoadedGridLayer) layer;
				if ("true".equals(loadedLayer.getProperty("alt"))) {
					altBuffer.begin();
				}
				layer.render(batch);
				if ("true".equals(loadedLayer.getProperty("alt"))) {
					altBuffer.end();
					getScreen().resumeNormalBuffer();
				}
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#excludeTile
	 * (net.wombatrpgs.mgne.maps.layers.GridLayer, net.wombatrpgs.mgne.maps.events.MapEvent, int, int)
	 */
	@Override
	public boolean excludeTile(GridLayer layer, MapEvent event, int tileX, int tileY) {
		int screenX = tileX * getTileWidth();
		int screenY = tileY * getTileHeight();
		int tx = (int) getScreen().getCamera().getTarget().getX();
		int ty = (int) getScreen().getCamera().getTarget().getY();
		screenX -= tx - (tx % 32);
		screenY -= ty - (ty % 32);
		screenX += getScreen().getWidth() / 2;
		screenY += getScreen().getHeight() / 2;
		lightBuffer.begin();
		Pixmap map = ScreenUtils.getFrameBufferPixmap(screenX, screenY, 2, 2);
		lightBuffer.end();
		boolean alt = "true".equals(layer.getProperty("alt"));
		Color c = new Color(map.getPixel(0, 0));
		if ((!alt && c.r > .6) || (alt && c.r < .4)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.LoadedLevel#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		
		lightBuffer = new FrameBuffer(Format.RGB565,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
		
		altBuffer = new FrameBuffer(Format.RGB565,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
	}
	
}
