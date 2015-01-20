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

import net.wombatrpgs.bacon01.EffectLight;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;

/**
 * Bacon game level, currently hacked for light testing.
 */
public class BaconLevel extends LoadedLevel {
	
	protected FrameBuffer buffer;
	protected EffectLight effect;

	public BaconLevel(LoadedMapMDO mdo, Screen screen) {
		super(mdo, screen);
		
		buffer = new FrameBuffer(Format.RGB565,
				getScreen().getWidth(),
				getScreen().getHeight(),
				false);
		
		effect = new EffectLight(buffer);
		getScreen().addEffect(effect);
		assets.add(effect);
	}
	
	public FrameBuffer getLightBuffer() { return buffer; }
	
	/**
	 * @see net.wombatrpgs.mgne.maps.Level#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
		buffer.begin();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getScreen().resumeNormalBuffer();
		
		super.render(batch);
		
//		getScreen().getUIBatch().begin();
//		getScreen().getUIBatch().draw(buffer.getColorBufferTexture(), 0, 0);
//		getScreen().getUIBatch().end();
	}
	
	
}
