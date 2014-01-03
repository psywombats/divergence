/**
 *  EffectOcclusion.java
 *  Created on Oct 9, 2013 1:40:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.effects;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.AnimationStrip;
import net.wombatrpgs.rainfall.graphics.ShaderFromData;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.screen.TrackerCam;
import net.wombatrpgs.rainfall.screen.WindowSettings;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.ShaderMDO;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectOcclusionMDO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * An effect that displays only on ground.
 */
public class EffectOcclusion extends Effect {
	
	protected EffectOcclusionMDO mdo;
	protected AnimationStrip groundAnim, allAnim;
	protected ShaderProgram groundShader, allShader;
	protected Texture viewTex;

	/**
	 * Creates a new occlusion effect from data.
	 * @param	parent			The level to create for
	 * @param	mdo				The data to create from
	 */
	public EffectOcclusion(Level parent, EffectOcclusionMDO mdo) {
		super(parent, mdo);
		this.mdo = mdo;
		groundAnim = new AnimationStrip(RGlobal.data.getEntryFor(mdo.groundAnim, AnimationMDO.class));
		allAnim = new AnimationStrip(RGlobal.data.getEntryFor(mdo.allAnim, AnimationMDO.class));
		groundShader = new ShaderFromData(RGlobal.data.getEntryFor(mdo.groundShader, ShaderMDO.class));
		allShader = new ShaderFromData(RGlobal.data.getEntryFor(mdo.allShader, ShaderMDO.class));
		
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		TextureRegion tex = allAnim.getRegion();
		float width = (float) tex.getRegionWidth() / camera.zoom;
		float height = (float) tex.getRegionHeight() / camera.zoom;
		batch.setShader(allShader);
		batch.begin();
		for (int x = 0; x < RGlobal.window.getWidth(); x += width) {
			for (int y = 0; y < RGlobal.window.getHeight(); y += height) {
				batch.draw(tex, x, y, width, height);
			}
		}
		batch.end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		allAnim.queueRequiredAssets(manager);
		groundAnim.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		allAnim.postProcessing(manager, pass);
		groundAnim.postProcessing(manager, pass);
		
		groundShader.begin();
		groundShader.setUniformi("u_tilesize", parent.getTileWidth(), parent.getTileHeight());
		groundShader.setUniformi("u_mapsize", parent.getWidth(), parent.getHeight());
		groundShader.end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.effects.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!allAnim.isMoving() || !groundAnim.isMoving()) {
			allAnim.startMoving();
			groundAnim.startMoving();
		}
		allAnim.update(elapsed);
		groundAnim.update(elapsed);
		TrackerCam cam  = RGlobal.screens.peek().getCamera();
		//System.out.println(cam.position.x + " " + cam.position.y);
		groundShader.begin();
		groundShader.setUniformf("u_scale", cam.zoom);
		groundShader.setUniformi("u_visibility", 1);
		groundShader.setUniformf("u_offset",
				(cam.position.x - RGlobal.window.getViewportWidth()/2.f) / cam.zoom,
				(cam.position.y - RGlobal.window.getViewportHeight()/2.f) / cam.zoom);
		groundShader.end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.effects.Effect#renderOver
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void renderOver(OrthographicCamera camera) {
		super.renderOver(camera);
		batch.setShader(groundShader);
		WindowSettings win = RGlobal.window;
		
		TextureRegion tex = groundAnim.getRegion();
		float width = (float) tex.getRegionWidth() / camera.zoom;
		float height = (float) tex.getRegionHeight() / camera.zoom;
		
		
		batch.begin();
		parent.getPassTex().bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		for (int x = 0; x < win.getWidth(); x += width) {
			for (int y = 0; y < win.getHeight(); y += height) {
				batch.draw(tex, x, y, width, height);
			}
		}
		batch.end();
	}

}
