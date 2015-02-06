/**
 *  BaconLevel.java
 *  Created on Jan 15, 2015 1:42:09 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import net.wombatrpgs.bacon01.EffectAltLight;
import net.wombatrpgs.bacon01.maps.events.EventStalker;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.maps.events.Avatar;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.maps.layers.GridLayer;
import net.wombatrpgs.mgne.maps.layers.LoadedGridLayer;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;

/**
 * Bacon game level, currently hacked for light testing.
 */
public class BaconLevel extends LoadedLevel {
	
	protected ShaderFromData shader;
	protected FrameBuffer lightBuffer, altBuffer, normBuffer;
	protected EffectAltLight effect;
	protected Graphic light;
	protected Color clearColor;
	protected float totalElapsed;
	
	protected int lastX, lastY;

	public BaconLevel(LoadedMapMDO mdo, Screen screen) {
		super(mdo, screen);
		
		shader = new ShaderFromData(MGlobal.data.getEntryFor("shader_lighttest", ShaderMDO.class));
		
		light = new Graphic("light.png");
		assets.add(light);
		
		clearColor = new Color(0, 0, 0, 1);
	}
	
	public FrameBuffer getLightBuffer() { return lightBuffer; }
	public FrameBuffer getAltBuffer() { return altBuffer; }
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
		renderGrid(batch, false);
		
		lightBuffer.begin();
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		lightBuffer.end();
		
		getScreen().resumeNormalBuffer();
		
		renderEvents(getScreen().getViewBatch());
		for (MapEvent event : getEventLayer().getAll()) {
			if (event instanceof EventStalker) {
				EventStalker stalker = (EventStalker) event;
				stalker.trueRender(getScreen().getViewBatch());
			}
		}
		
		renderGrid(batch, true);
		
		getScreen().resumeNormalBuffer();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		totalElapsed += elapsed;
		
		Avatar hero = MGlobal.getHero();
		int x1 = (int) Math.floor((float) (hero.getHitbox().getX()) / 16f);
		int y2 = (int) Math.floor((float) (getHeightPixels() - hero.getHitbox().getY()) / 16f);
		int x2 = (int) Math.floor((float) (hero.getHitbox().getX() + hero.getHitbox().getWidth()) / 16f);
		int y1 = (int) Math.floor((float) (getHeightPixels() - 
				(hero.getHitbox().getY() + hero.getHitbox().getHeight())) / 16f);
		List<Vector2> checks = new ArrayList<Vector2>();
		checks.add(new Vector2(x1, y1));
		checks.add(new Vector2(x1, y2));
		checks.add(new Vector2(x2, y1));
		checks.add(new Vector2(x2, y2));
		
		boolean passed = false;
		for (Vector2 check : checks) {
			int tileX = (int) check.x;
			int tileY = (int) check.y;
			if (isTilePassable(tileX, tileY)) {
				passed = true;
				break;
			}
			boolean excluded = true;
			for (GridLayer layer : gridLayers) {
				if (layer.getZ() > .5) continue;
				if (!excludeTile(layer, hero, tileX, tileY) && !layer.isTilePassable(tileX, tileY)) {
					excluded = false;
					break;
				}
			}
			if (excluded) passed = true;
			if (passed) break;
		}
		if (!passed) {
			hero.respawn();
		}
		
		// periodic
		if (getProperty("period") != null) {
			float period = Float.valueOf(getProperty("period"));
			float ratio = (float) Math.sin(totalElapsed / period * Math.PI*2);
			ratio = ratio/2f + .5f;
			clearColor.r = ratio;
			clearColor.g = ratio;
			clearColor.b = ratio;
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#renderGrid
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, boolean)
	 */
	@Override
	protected void renderGrid(SpriteBatch batch, boolean upper) {
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glColorMask(true, true, true, true);
		
		altBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, upper? 0f : 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		altBuffer.end();
		
		normBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, upper? 0f : 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		normBuffer.end();
		
		for (GridLayer layer : gridLayers) {
			if (layer.getZ() < 1.f ^ upper) {
				LoadedGridLayer loadedLayer = (LoadedGridLayer) layer;
				if ("true".equals(loadedLayer.getProperty("alt"))) {
					altBuffer.begin();
				} else {
					normBuffer.begin();
				}
				layer.render(batch);
				if ("true".equals(loadedLayer.getProperty("alt"))) {
					altBuffer.end();
				} else {
					normBuffer.end();
				}
				getScreen().resumeNormalBuffer();
			}
		}
		
		getScreen().resumeNormalBuffer();
		getScreen().getUIBatch().begin();
		getScreen().getUIBatch().setShader(shader);
		altBuffer.getColorBufferTexture().bind(2);
		lightBuffer.getColorBufferTexture().bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		getScreen().getUIBatch().draw(normBuffer.getColorBufferTexture(),
				0, 0,
				getScreen().getWidth(), getScreen().getHeight(),
				0, 0,
				getScreen().getWidth(), getScreen().getHeight(),
				false, true);
		getScreen().getUIBatch().setShader(null);
		getScreen().getUIBatch().end();
		
		getScreen().resumeNormalBuffer();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.Level#excludeTile
	 * (net.wombatrpgs.mgne.maps.layers.GridLayer, net.wombatrpgs.mgne.maps.events.MapEvent, int, int)
	 */
	@Override
	public boolean excludeTile(GridLayer layer, MapEvent event, int tileX, int tileY) {
		float r = sampleAt(tileX * getTileWidth(), getHeightPixels() - (tileY * getTileHeight()));
		boolean alt = "true".equals(layer.getProperty("alt"));
		if ((!alt && r > .5) || (alt && r < .5)) {
			lastX = tileX;
			lastY = tileY;
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
		//lightBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		normBuffer = new FrameBuffer(Format.RGBA4444,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
		normBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		altBuffer = new FrameBuffer(Format.RGBA4444,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
		altBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		shader.begin();
		shader.setUniformi("u_light", 1);
		shader.setUniformi("u_textureAlt", 2);
		shader.end();
	}
	
	protected float sampleAt(int x, int y) {
		int screenX = x;
		int screenY = y;
		int tx = (int) MGlobal.getHero().lastX;
		int ty = (int) MGlobal.getHero().lastY;
		screenX -= tx;
		screenY -= ty;
		screenX += getScreen().getWidth() / 2;
		screenY += getScreen().getHeight() / 2;
		lightBuffer.begin();
		Pixmap map = ScreenUtils.getFrameBufferPixmap(screenX, screenY, 2, 2);
		lightBuffer.end();
		Color c = new Color(map.getPixel(0, 0));
		return c.r;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.LoadedLevel#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		shader.dispose();
		lightBuffer.dispose();
		altBuffer.dispose();
		normBuffer.dispose();
		effect.dispose();
	}
	
	
}
