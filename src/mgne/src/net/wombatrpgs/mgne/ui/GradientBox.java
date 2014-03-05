/**
 *  GradientBox.java
 *  Created on Mar 5, 2014 3:02:51 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.graphics.Disposable;
import net.wombatrpgs.mgne.util.Simplex;

/**
 * A box that displays with a nice texture and other stuff. Performs the semi-
 * expensive Perlin computations as part of the queueing process so if needed
 * it can happen in a loading bar.
 */
public class GradientBox implements Queueable, Disposable {
	
	protected int width, height;
	protected Color c1, c2;
	protected Pixmap map;
	protected Texture tex;
	
	/**
	 * Creates a new gradient box with no border. The box will use default
	 * Perlin settings to create the texture.
	 * @param	width			The width of the gbox (in real px)
	 * @param	height			The height of the gbox (in real px)
	 * @param	c1				The base color used for the corners
	 * @param	c2				The base color used for the center
	 */
	public GradientBox(int width, int height, Color c1, Color c2) {
		this.width = width;
		this.height = height;
		this.c1 = c1;
		this.c2 = c2;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// heh we don't have to do anything I don't think
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		float denom = (float) Math.sqrt(width*width + height*height);
		map = new Pixmap(width, height, Format.RGBA8888);
		for (int x = 0; x < width; x += 1) {
			for (int y = 0; y < height; y += 1) {
				float r = (float) (Math.sqrt(distToMidline(x, y)) / denom);
				float off = Simplex.scaledOctaveNoise2D(2, .5f, .05f, -.02f, 0, x, y);
				map.drawPixel(x, y, Color.rgba8888(
						lerp(c1.r, c2.r, r) - off,
						lerp(c1.g, c2.g, r) - off,
						lerp(c1.b, c2.b, r) - off,
						lerp(c1.a, c2.a, r)));
			}
		}
		tex = new Texture(map);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		tex.dispose();
		map.dispose();
	}
	
	/**
	 * Renders itself at a specific location.
	 * @param 	batch			The batch to render the graphic as part of
	 * @param 	x				The x-coord to render at (in px)
	 * @param 	y				The y-coord to render at (in px)
	 */
	public void renderAt(SpriteBatch batch, float x, float y) {
		batch.begin();
		batch.draw(tex, x, y);
		batch.end();
	}

	protected static float lerp(float a, float b, float r) {
		return a*(1f-r) + b*r;
	}
	
	protected static float dist2(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return dx*dx + dy*dy;
	}
	
	protected float distToMidline(float x, float y) {
		float l2 = dist2(0, 0, width, height);
		float t = (x * width + y * height) / l2;
		if (t < 0) return dist2(x, y, 0, 0);
		if (t > 1) return dist2(x, y, width, height);
		return dist2(x, y, t * width, t * height);
	}

}
