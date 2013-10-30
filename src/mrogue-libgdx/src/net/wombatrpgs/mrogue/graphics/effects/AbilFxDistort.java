/**
 *  AbilFxTest.java
 *  Created on Oct 18, 2013 7:02:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.graphics.PostRenderable;
import net.wombatrpgs.mrogue.graphics.ShaderFromData;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogue.screen.WindowSettings;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxDistortMDO;

/**
 * Some really cool proof of concept.
 */
public class AbilFxDistort extends AbilFX implements PostRenderable {
	
	protected AbilFxDistortMDO mdo;
	protected Graphic sphere;
	protected ShaderFromData shader;
	protected FrameBuffer buffer;

	/**
	 * Creates an effect from data, parent.
	 * @param	mdo					The data to generate from
	 * @param	abil				The ability to generate for
	 */
	public AbilFxDistort(AbilFxDistortMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
		sphere = new Graphic(Constants.TEXTURES_DIR, mdo.graphic);
		shader = new ShaderFromData(MGlobal.data.getEntryFor(mdo.shader, ShaderMDO.class));
		assets.add(sphere);
		
		privateBatch.setShader(shader);

	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.AbilFX#spawn()
	 */
	@Override
	public void spawn() {
		super.spawn();
		MGlobal.levelManager.getScreen().registerPostRender(this);
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.PostRenderable#renderPost()
	 */
	@Override
	public void renderPost() {
		
		Texture t = sphere.getTexture();
		WindowSettings win = MGlobal.window;
		Screen sc = MGlobal.screens.peek();
		float scale = (parent.getTileWidth()*1.5f) / (float) t.getWidth();
		if (totalElapsed < mdo.fadein) {
			scale *= abil.getRadius()*2 * totalElapsed / mdo.fadein;
		} else if (totalElapsed > (mdo.duration-mdo.fadein)) {
			scale *= abil.getRadius()*2 * (mdo.duration-totalElapsed) / mdo.fadein;
		} else {
			scale *= abil.getRadius()*2;
		}
		
		float atX = getX() + parent.getTileWidth()/2f - t.getWidth()*scale/2f;
		float atY = getY() + parent.getTileHeight()/2f - t.getHeight()*scale/2f;
		
		shader.begin();
		shader.setUniformi("u_mask", 1);
		shader.setUniformf("u_screensize", win.getResolutionWidth(), win.getResolutionHeight());
		shader.setUniformf("u_power", (float) (.9-Math.sin(done*3f)/3f+.1f));
		shader.setUniformf("u_done", done);
		shader.end();
		
		sc.getBuffer().end();
		buffer.begin();
		sc.getUIBatch().begin();
		sc.getUIBatch().draw(sc.getLastBuffer().getColorBufferTexture(),
				0, 0,
				win.getWidth(), win.getHeight());
		sc.getUIBatch().end();
		buffer.end();
		sc.getBuffer().begin();
		
		privateBatch.setProjectionMatrix(sc.getCamera().combined);
		privateBatch.begin();
		
		t.bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		
		privateBatch.draw(buffer.getColorBufferTexture(), atX, atY, scale*t.getWidth(), scale*t.getHeight());
		
		privateBatch.end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		buffer.dispose();
		shader.dispose();
		MGlobal.levelManager.getScreen().removePostRender(this);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		buffer = new FrameBuffer(Format.RGB565, 
				sphere.getWidth(),
				sphere.getHeight(),
				false);
	}

}
