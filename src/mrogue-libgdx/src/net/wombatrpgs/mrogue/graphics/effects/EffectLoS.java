/**
 *  EffectLoS.java
 *  Created on Oct 6, 2013 5:52:55 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	protected AnimationStrip anim;
	protected ShaderProgram shader;
	protected float offset;

	/**
	 * Generates a new effect from data.
	 * @param	parent			The parent level this effect is for
	 * @param	mdo				The data to create effect from
	 */
	public EffectLoS(Level parent, EffectLoSMDO mdo) {
		super(parent, mdo);
		this.mdo = mdo;
		anim = new AnimationStrip(MGlobal.data.getEntryFor(mdo.tex, AnimationMDO.class));
		shader = new ShaderFromData(MGlobal.data.getEntryFor(mdo.shader, ShaderMDO.class));
		batch.setShader(shader);
		offset = 0;
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		WindowSettings win = MGlobal.window;
		TextureRegion tex = anim.getRegion();
		batch.begin();

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
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		anim.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		anim.postProcessing(manager, pass);
		shader.begin();
		shader.setUniformf("u_tilesize", parent.getTileWidth(), parent.getTileHeight());
		shader.setUniformf("u_mapsize", parent.getWidth(), parent.getHeight());
		shader.end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		
		offset += mdo.velocity * elapsed;
		if (offset > MGlobal.window.getWidth()) {
			offset -= MGlobal.window.getWidth();
		}
		
		float[] visible = MGlobal.hero.getVisibleData();
		TrackerCam cam  = MGlobal.screens.peek().getCamera();
		super.update(elapsed);
		shader.begin();
		shader.setUniform1fv("u_visibility", visible, 0, visible.length);
		shader.setUniformf("u_offset",
				cam.position.x - MGlobal.window.getWidth()/2.f,
				cam.position.y - MGlobal.window.getHeight()/2.f);
		shader.end();
	}

}
