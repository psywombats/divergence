/**
 *  EffectLight.java
 *  Created on Jan 15, 2015 2:11:04 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.Effect;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;

/**
 * Hack for light demo.
 */
public class EffectAltLight extends Effect {
	
	protected static String SHADER_MDO = "shader_lighttest";
	
	protected BaconLevel level;

	public EffectAltLight(BaconLevel level) {
		super(MGlobal.data.getEntryFor(SHADER_MDO, ShaderMDO.class));
		this.level = level;
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.Effect#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		
		shader.begin();
		shader.setUniformi("u_light", 1);
		shader.setUniformi("u_textureAlt", 2);
		shader.end();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.Effect#apply(com.badlogic.gdx.graphics.glutils.FrameBuffer)
	 */
	@Override
	public void apply(FrameBuffer original) {
		
		WindowSettings window = MGlobal.window;
	
		// copy to the temp buffer
		buffer.begin();
		standardBatch.begin();
		standardBatch.draw(
				original.getColorBufferTexture(),		// texture
				0, 0,									// x/y in screen space
				0, 0,									// origin x/y screen
				window.getWidth(), window.getHeight(),	// width/height screen
				1, 1,									// scale x/y
				0,										// rotation in degrees
				0, 0,									// x/y in texel space
				window.getWidth(), window.getHeight(),	// width/height texel
				false, true								// flip horiz/vert
			);
		standardBatch.end();
		buffer.end();
		
		// temp buffer back to original buffer
		original.begin();
		effectBatch.begin();
		level.getLightBuffer().getColorBufferTexture().bind(1);
		level.getAltBuffer().getColorBufferTexture().bind(2);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		effectBatch.draw(
				buffer.getColorBufferTexture(),			// texture
				0, 0,									// x/y in screen space
				0, 0,									// origin x/y screen
				window.getWidth(), window.getHeight(),	// width/height screen
				1, 1,									// scale x/y
				0,										// rotation in degrees
				0, 0,									// x/y in texel space
				window.getWidth(), window.getHeight(),	// width/height texel
				false, true								// flip horiz/vert
			);
		effectBatch.end();
		original.end();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
	}
	
}
