/**
 *  AbilFxFlames.java
 *  Created on Oct 18, 2013 4:45:39 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.graphics.ShaderFromData;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.graphics.ShaderMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxFlamesMDO;

/**
 * Flame texture and noise thing. We'll see how this goes.
 */
public class AbilFxFlames extends AbilFX {
	
	AbilFxFlamesMDO mdo;
	protected Graphic flames;
	protected Graphic noise1, noise2, noise3;
	protected Graphic mask;
	protected ShaderFromData shader;

	/**
	 * Creates an effect given data, parent.
	 * @param	mdo				The data to create from
	 * @param	abil			The parent to create for
	 */
	public AbilFxFlames(AbilFxFlamesMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
		flames = new Graphic(Constants.TEXTURES_DIR, mdo.flames);
		assets.add(flames);
		noise1 = new Graphic(Constants.TEXTURES_DIR, mdo.noise1);
		assets.add(noise1);
		noise2 = new Graphic(Constants.TEXTURES_DIR, mdo.noise2);
		assets.add(noise2);
		noise3 = new Graphic(Constants.TEXTURES_DIR, mdo.noise3);
		assets.add(noise3);
		mask = new Graphic(Constants.TEXTURES_DIR, mdo.mask);
		assets.add(mask);
		shader = new ShaderFromData(MGlobal.data.getEntryFor(mdo.shader, ShaderMDO.class));
		
		privateBatch.setShader(shader);
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.AbilFX#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		if (MGlobal.won) return;
		
		Screen sc = MGlobal.screens.peek();
		getParent().getBatch().end();
		
		Texture t = flames.getTexture();
		
		float scale, restrict, atX, atY;
		switch(abil.getType()) {
		case BALL: case MELEE: case PROJECTILE:
			scale = abil.getRadius() * 2.5f * ((float) parent.getTileWidth() / (float) t.getWidth());
			restrict = 1.f;
			if (totalElapsed < mdo.fadein) {
				//restrict = totalElapsed/mdo.fadein;
				scale *= (totalElapsed/mdo.fadein);
			} else if (totalElapsed > (mdo.duration-mdo.fadein)) {
				restrict = (mdo.duration-totalElapsed) / mdo.fadein;
			} else {
				//restrict = 1f;
			}
			atX = getX() + parent.getTileWidth()/2f - t.getWidth()*scale/2f;
			atY = getY() + parent.getTileHeight()/2f - t.getHeight()*scale/2f;
			break;
		default:
			atX = 0; atY = 0; restrict = 0; scale = 1;
		}
		
		shader.begin();
		shader.setUniformf("u_restrict", restrict);
		shader.setUniformi("u_mask", 1);
		shader.setUniformi("u_noise1", 2);
		shader.setUniformi("u_noise2", 3);
		shader.setUniformi("u_noise3", 4);
		shader.setUniformf("u_elapsed", totalElapsed);
		shader.end();
		
		privateBatch.begin();
		privateBatch.setProjectionMatrix(sc.getCamera().combined);
		
		Texture maskTex = mask.getTexture();
		Texture noise1Tex = noise1.getTexture();
		Texture noise2Tex = noise2.getTexture();
		Texture noise3Tex = noise3.getTexture();
		maskTex.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		noise1Tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		noise2Tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		noise3Tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		maskTex.bind(1);
		noise1Tex.bind(2);
		noise2Tex.bind(3);
		noise3Tex.bind(4);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		
		privateBatch.draw(t, atX, atY, scale*t.getWidth(), scale*t.getHeight());
		
		privateBatch.end();
		parent.getBatch().begin();
	}

}
