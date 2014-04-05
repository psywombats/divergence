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

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.graphics.interfaces.PosRenderable;
import net.wombatrpgs.mgne.util.Simplex;
import net.wombatrpgs.mgneschema.ui.GradientBoxMDO;

/**
 * A box that displays with a nice texture and other stuff. Performs the semi-
 * expensive Perlin computations as part of the queueing process so if needed
 * it can happen in a loading bar.
 */
public class GradientBox implements Queueable, Disposable, PosRenderable {
	
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
	 * Creates a new gradient box based on data. The box will use default Perlin
	 * settings to create the texture. No initial size is given, so make sure to
	 * resizeTo before displaying.
	 * @param	mdo				The data to generate from
	 */
	public GradientBox(GradientBoxMDO mdo) {
		c1 = new Color(mdo.r1, mdo.g1, mdo.b1, 1f);
		c2 = new Color(mdo.r2, mdo.g2, mdo.b2, 1f);
	}
	
	/**
	 * Creates a new gradient box based on data from a database key.
	 * @param	mdoKey			The key of the mdo to use
	 */
	public GradientBox(String mdoKey) {
		this(MGlobal.data.getEntryFor(mdoKey, GradientBoxMDO.class));
	}
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return height; }

	/** @return The gradient texture if you really want to display yourself */
	public Texture getTex() { return tex; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		batch.begin();
		batch.draw(tex, x, y, width, height);
		batch.end();
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
		if (width > 0 && height > 0) {
			resizeTo(width, height);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		tex.dispose();
		map.dispose();
	}
	
	/**
	 * Resizes the gradient to a new size. This will discard the existing
	 * underlying textures. Semi-expensive.
	 * @param	width			The new width to size to (in px)
	 * @param	height			The new height to size to (in px)
	 */
	public void resizeTo(int width, int height) {
		this.width = width;
		this.height = height;
		
		if (tex != null) {
			dispose();
		}
		
		float denom = (float) Math.sqrt(width/2*width/2 + height/2*height/2)/2f;
		map = new Pixmap(width, height, Format.RGBA8888);
		for (int x = 0; x < width; x += 1) {
			for (int y = 0; y < height; y += 1) {
				float r = (float) (Math.sqrt(distToMidline(x, y)) / denom);
				float off = Simplex.scaledOctaveNoise2D(2, .7f, .02f, -.1f, .1f, x, y);
				map.drawPixel(x, y, Color.rgba8888(
						lerp(c1.r, c2.r, r+off),
						lerp(c1.g, c2.g, r+off),
						lerp(c1.b, c2.b, r+off),
						lerp(c1.a, c2.a, r+off)));
			}
		}
		tex = new Texture(map);
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
