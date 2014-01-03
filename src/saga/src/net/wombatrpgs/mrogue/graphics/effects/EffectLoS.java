/**
 *  EffectLoS.java
 *  Created on Oct 6, 2013 5:52:55 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.AnimationStrip;
import net.wombatrpgs.mrogue.graphics.ShaderFromData;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.screen.TrackerCam;
import net.wombatrpgs.mrogue.screen.WindowSettings;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.EffectLoSMDO;

/**
 * An obscuring effect for line of sight.
 */
public class EffectLoS extends Effect {
	
	// max of width*height, must be changed in shader too
	protected static final int MAX_TILES = 2400;
	
	protected EffectLoSMDO mdo;
	protected AnimationStrip invisibleAnim, unseenAnim;
	protected ShaderProgram shader;
	protected Texture viewTex;
	protected boolean firstDone, updated;
	protected float offset;

	/**
	 * Generates a new effect from data.
	 * @param	parent			The parent level this effect is for
	 * @param	mdo				The data to create effect from
	 */
	public EffectLoS(Level parent, EffectLoSMDO mdo) {
		super(parent, mdo);
		this.mdo = mdo;
		firstDone = false;
		invisibleAnim = new AnimationStrip(MGlobal.data.getEntryFor(mdo.invisibleTex, AnimationMDO.class));
		unseenAnim = new AnimationStrip(MGlobal.data.getEntryFor(mdo.unseenTex, AnimationMDO.class));
		if (MGlobal.graphics.isShaderEnabled()) {
			if (MGlobal.graphics.isShaderDebugEnabled()) {
				MGlobal.reporter.inform("Attempting to create LoS shader...");
			}
			shader = new ShaderFromData(MGlobal.data.getEntryFor(mdo.shader, ShaderMDO.class));
			if (MGlobal.graphics.isShaderDebugEnabled()) {
				MGlobal.reporter.inform("LoS shader successfully init'd");
			}
			batch.setShader(shader);
		}
		offset = 0;
		updated = false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (!updated) return;
		if (!MGlobal.graphics.isShaderEnabled()) return;
		if (MGlobal.graphics.isShaderDebugEnabled() && !firstDone) {
			MGlobal.reporter.inform("LoS shader draw start");
		}
		WindowSettings win = MGlobal.window;
		TextureRegion tex = invisibleAnim.getRegion();
		TextureRegion tex2 = unseenAnim.getRegion();
		
		shader.begin();
		shader.setUniformi("u_colorcomp", 0);
		shader.end();
		batch.begin();
		MGlobal.hero.getVisibleData().bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

		// forward +wrap
		batch.draw(
				tex,
				offset,
				0,
				win.getWidth() / 2,
				win.getHeight() / 2, 
				win.getWidth(),
				win.getHeight(),
				1,
				1,
				0);
		batch.draw(
				tex,
				offset - win.getWidth(), 
				0,
				win.getWidth() / 2,
				win.getHeight() / 2, 
				win.getWidth(),
				win.getHeight(),
				1,
				1,
				0);
		
		// mirror + wrap
		batch.draw(
				tex,
				-offset, 
				0,
				win.getWidth() / 2,
				win.getHeight() / 2, 
				win.getWidth(),
				win.getHeight(),
				1,
				1,
				180);
		batch.draw(
				tex,
				win.getWidth() - offset, 
				0,
				win.getWidth() / 2,
				win.getHeight() / 2, 
				win.getWidth(),
				win.getHeight(),
				1,
				1,
				180);
		
		batch.end();
		
		shader.begin();
		shader.setUniformi("u_colorcomp", 1);
		shader.end();
		batch.begin();
		MGlobal.hero.getVisibleData().bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		batch.draw(
				tex2,
				0,
				0,
				win.getWidth() / 2,
				win.getHeight() / 2, 
				win.getWidth(),
				win.getHeight(),
				1,
				1,
				0);
		batch.end();
		
		if (MGlobal.graphics.isShaderDebugEnabled() && !firstDone) {
			MGlobal.reporter.inform("LoS shader draw end");
		}
		firstDone = true;
		
//		MGlobal.screens.peek().getViewBatch().begin();
//		MGlobal.screens.peek().getViewBatch().draw(MGlobal.hero.getVisibleData(), 0, 0);
//		MGlobal.screens.peek().getViewBatch().end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		invisibleAnim.queueRequiredAssets(manager);
		unseenAnim.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (!MGlobal.graphics.isShaderEnabled()) return;
		if (MGlobal.graphics.isShaderDebugEnabled() && !firstDone) {
			MGlobal.reporter.inform("LoS post-processing start");
		}
		invisibleAnim.postProcessing(manager, pass);
		unseenAnim.postProcessing(manager, pass);
		shader.begin();
		shader.setUniformi("u_tilesize", parent.getTileWidth(), parent.getTileHeight());
		shader.setUniformi("u_mapsize", parent.getWidth(), parent.getHeight());
		shader.end();
		if (MGlobal.graphics.isShaderDebugEnabled() && !firstDone) {
			MGlobal.reporter.inform("LoS post-processing end");
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (!MGlobal.graphics.isShaderEnabled()) return;
		if (MGlobal.graphics.isShaderDebugEnabled() && !firstDone) {
			MGlobal.reporter.inform("LoS shader update start");
		}
		offset += mdo.velocity * elapsed;
		if (offset > MGlobal.window.getWidth()) {
			offset -= MGlobal.window.getWidth();
		}
		
		TrackerCam cam  = MGlobal.screens.peek().getCamera();
		super.update(elapsed);
		shader.begin();
		shader.setUniformi("u_visibility", 1);
		shader.setUniformf("u_offset",
				cam.position.x - MGlobal.window.getWidth()/2.f,
				cam.position.y - MGlobal.window.getHeight()/2.f);
		shader.end();
		updated = true;
		if (MGlobal.graphics.isShaderDebugEnabled() && !firstDone) {
			MGlobal.reporter.inform("LoS shader update end");
		}
	}

}
