/**
 *  BaconLevel.java
 *  Created on Jan 15, 2015 1:42:09 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.bacon01.EffectAltLight;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.AnimationStrip;
import net.wombatrpgs.mgne.maps.LoadedLevel;
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
		
		lightBuffer = new FrameBuffer(Format.RGB565,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
		
		altBuffer = new FrameBuffer(Format.RGB565,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
		
		effect = new EffectAltLight(lightBuffer, altBuffer);
		getScreen().addEffect(effect);
		assets.add(effect);
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		light.setScale(4);
		assets.add(light);
	}
	
	public FrameBuffer getLightBuffer() { return lightBuffer; }
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
		lightBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		light.renderAt(getBatch(),
				MGlobal.getHero().getX() - (light.getWidth() - MGlobal.getHero().getWidth()) / 2,
				MGlobal.getHero().getY() - (light.getHeight() - MGlobal.getHero().getHeight()) / 2);
		
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
					lightBuffer.end();
					getScreen().resumeNormalBuffer();
				}
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		light.update(elapsed);
		total += elapsed;
		float scale = (float) (4f + Math.sin(total));
		light.setScale(scale);
	}
	
}
