/**
 *  EffectFog.java
 *  Created on Apr 18, 2013 11:27:12 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.AnimationStrip;
import net.wombatrpgs.mrogue.graphics.ShaderFromData;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.screen.TrackerCam;
import net.wombatrpgs.mrogue.screen.WindowSettings;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.EffectFogMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Fog graphical effect.
 */
public class EffectFog extends Effect {
	
	protected static int MAX_BEACONS = 50;
	
	protected EffectFogMDO mdo;
	protected AnimationStrip anim;
	protected ShaderProgram shader;
	protected float offset;
	
	/**
	 * Inherited constructor.
	 * @param	parent			The parent level
	 * @param 	mdo				The data to construct effect from
	 */
	public EffectFog(Level parent, EffectFogMDO mdo) {
		super(parent, mdo);
		this.mdo = mdo;
		anim = new AnimationStrip(MGlobal.data.getEntryFor(mdo.tex, AnimationMDO.class));
		offset = 0;
		batch.setColor(1, 1, 1, mdo.density);
		shader = new ShaderFromData(MGlobal.data.getEntryFor(mdo.shader, ShaderMDO.class));
		batch.setShader(shader);
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		WindowSettings win = MGlobal.window;
		TrackerCam cam = MGlobal.screens.peek().getCamera();
		TextureRegion tex = anim.getRegion();
		
		batch.begin();
		float atX = cam.position.x - win.getWidth()/2;
		float atY = cam.position.y - win.getHeight()/2;
		shader.setUniformf("u_atX", atX);
		shader.setUniformf("u_atY", atY);
		
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
		
		List<MapEvent> beacons = parent.getEventsByGroup("antifog_beacon");
		float beaconCoords[] = new float[beacons.size() * 2];
		for (int i = 0; i < beacons.size(); i++) {
			MapEvent beacon = beacons.get(i);
			beaconCoords[i*2] = beacon.getX();
			beaconCoords[i*2+1] = beacon.getY();
		}
		shader.begin();
		shader.setUniform2fv("u_beacons", beaconCoords, 0, beaconCoords.length);
		shader.setUniformi("u_beaconCount", beaconCoords.length/2);
		shader.setUniformi("u_radius", mdo.radius);
		shader.setUniformf("u_factor", mdo.factor);
		shader.setUniformf("u_exp", mdo.exponent);
		shader.end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		anim.update(elapsed);
		offset += mdo.velocity * elapsed;
		if (offset > MGlobal.window.getWidth()) {
			offset -= MGlobal.window.getWidth();
		}
	}

}
