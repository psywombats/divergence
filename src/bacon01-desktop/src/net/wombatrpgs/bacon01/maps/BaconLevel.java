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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
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
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;

/**
 * Bacon game level, currently hacked for light testing.
 */
public class BaconLevel extends LoadedLevel {
	
	protected FrameBuffer lightBuffer, altBuffer;
	protected EffectAltLight effect;
	protected Graphic finger;
	
	protected int lastX, lastY;
	
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
		
		finger = new Graphic("finger.png");
		assets.add(finger);
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
			screenX -= light.getWidth() / 2;
			screenY -= light.getHeight() / 2;
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
		float scale = (float) (6f + Math.sin(total / 3f) * 4f);
		light.setScale(scale);
		
		MapEvent hero = MGlobal.getHero();
		int x1 = (int) hero.getHitbox().getX();
		int x2 = (int) (x1 + hero.getHitbox().getWidth());
		int y1 = (int) hero.getHitbox().getY();
		int y2 = (int) (x1 + hero.getHitbox().getHeight());
		List<Vector2> checks = new ArrayList<Vector2>();
		checks.add(new Vector2(x1, y1));
		checks.add(new Vector2(x1, y2));
		checks.add(new Vector2(x2, y1));
		checks.add(new Vector2(x2, y2));
		
		boolean passed = false;
		for (Vector2 check : checks) {
			int tileX = (int) (check.x - check.x % getTileWidth()) / getTileWidth();
			int tileY = (int) (check.y - check.y % getTileHeight()) / getTileHeight();
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
			while (!isChipPassable(hero.getTileX(), hero.getTileY())) {
				hero.setTileLocation(MGlobal.rand.nextInt(getWidth()), MGlobal.rand.nextInt(getHeight()));
			}
		}
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
		float r = sampleAt(tileX * getTileWidth(), tileY * getTileHeight());
		boolean alt = "true".equals(layer.getProperty("alt"));
		if ((!alt && r > .4) || (alt && r < .6)) {
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
		
		altBuffer = new FrameBuffer(Format.RGB565,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
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
}
