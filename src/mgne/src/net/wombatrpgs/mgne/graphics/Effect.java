/**
 *  Effect.java
 *  Created on Jun 28, 2014 6:28:08 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;

/**
 * A frame-buffer based screen effect.
 */
public class Effect extends AssetQueuer implements Disposable, Updateable {
	
	protected ShaderMDO mdo;
	protected transient ShaderFromData shader;
	protected transient FrameBuffer buffer;
	protected transient SpriteBatch effectBatch, standardBatch;
	
	/**
	 * Creates a new effect that will copy screen data using the ... apparently
	 * forgot to finish writing this.
	 * @param	mdo				The data to create for effect copying
	 */
	public Effect(ShaderMDO mdo) {
		shader =  new ShaderFromData(mdo);
	}
	
	/**
	 * Creates an effect from raw shader files.
	 * @param	vertexFile		The shader vert file, minus shader dir
	 * @param	fragFile		The shader frag file, minus shader dir
	 */
	public Effect(String vertexFile, String fragFile) {
		shader = new ShaderFromData(vertexFile, fragFile);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.AssetQueuer#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		standardBatch = new SpriteBatch();
		effectBatch = new SpriteBatch();
		effectBatch.setShader(shader);
		buffer = new FrameBuffer(Format.RGB565, 
				MGlobal.window.getWidth(),
				MGlobal.window.getHeight(),
				false);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// default is nothing
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		buffer.dispose();
		effectBatch.dispose();
	}
	
	/**
	 * Returns the shader so screens can tamper it. Ugly. Sorry.
	 * @return					The shader used in this effect
	 */
	public ShaderFromData getShader() {
		return shader;
	}
	
	/**
	 * Applies this effect to the supplied frame buffer. Should perform this by
	 * copying from the original to the local buffer, then writing from the
	 * local buffer to the original while applying the effect.
	 * @param	original		The source buffer to copy from/to
	 */
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

}
