/**
 *  NumberSet.java
 *  Created on Aug 27, 2013 11:41:38 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogueschema.ui.NumberSetMDO;

/**
 * A set of numbers for rendering numbers to the screen? It's a number sprite,
 * essentially. Give it an int during your render phase and it'll spit it out
 * onto the screen using the sprite font. This is handled differently from font
 * to give better 1) visual control and 2) motion control.
 */
public class NumberSet implements Queueable {
	
	protected NumberSetMDO mdo;
	protected Texture tex;
	protected TextureRegion[] numberTextures;
	protected String filename;
	
	/**
	 * Creates a number set from data.
	 * @param 	mdo				The data to create from
	 */
	public NumberSet(NumberSetMDO mdo) {
		this.mdo = mdo;
		this.filename = Constants.UI_DIR + mdo.file;
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		manager.load(filename, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		tex = manager.get(filename, Texture.class);
		numberTextures = TextureRegion.split(tex, mdo.width, mdo.height)[0];
	}
	
	/**
	 * Renders a single digit at a given location. It's more efficient for
	 * other methods in this class to do everything in one batch though.
	 * @param 	digit			The digit (0-9) to render
	 * @param 	x				The x-coord to render at, in px
	 * @param 	y				The y-coord to render at, in px
	 */
	public void renderDigitAt(int digit, int x, int y) {
		if (digit < 0 || digit > 9) {
			MGlobal.reporter.warn("Rendering negative digit! " + digit);
		}
		SpriteBatch batch = MGlobal.screens.peek().getUIBatch();
		batch.begin();
		batch.draw(numberTextures[digit], x, y);
		batch.end();
	}
	
	/**
	 * Renders a number at a given location. Takes into account kerning and
	 * things like that. Only handles positive numbers right now. Also has tint!
	 * @param 	num				The number to render
	 * @param 	x				The x-coord to render at, in px
	 * @param 	y				The y-coord to render at, in px
	 * @param	r				The red compomenent of the tint, 0-1
	 * @param	g				The green compomenent of the tint, 0-1
	 * @param	b				The blue compomenent of the tint, 0-1
	 */
	public void renderNumberAt(int num, int x, int y, float r, float g, float b) {
		if (num < 0) {
			MGlobal.reporter.warn("Negative number rendered!"  + num);
			return;
		}
		int atX = x;
		String numStr = String.valueOf(num);
		SpriteBatch batch = MGlobal.screens.peek().getUIBatch();
		Color oldColor = batch.getColor();
		batch.setColor(r, g, b, 1);
		batch.begin();
		for (int i = 0; i < numStr.length(); i++) {
			// modular math my ass
			int digit = Integer.valueOf(numStr.charAt(i)) - 48;
			batch.draw(numberTextures[digit], atX, y);
			atX += (mdo.kerning + mdo.width);
		}
		batch.end();
		batch.setColor(oldColor);
	}

}
