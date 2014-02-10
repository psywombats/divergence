/**
 *  Nineslice.java
 *  Created on Jan 19, 2014 4:22:50 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.Queueable;
import net.wombatrpgs.mgne.core.SGlobal;
import net.wombatrpgs.mgneschema.ui.NinesliceMDO;

/**
 * Stretchy backgrounds! This is meant to be owned by a UI object, not displayed
 * raw. Therefore it doesn't have any positioning code and is meant to be
 * rendered at an offset from a parent, like a textbox.
 */
public class Nineslice implements Queueable {
	
	protected NinesliceMDO mdo;
	protected int width, height;
	protected String filename;
	
	protected Texture tex;
	protected TextureRegion[][] slices;
	protected SpriteBatch batch;
	protected FrameBuffer buffer;
	protected Texture appearance;
	
	/**
	 * Creates an unsized, initialized nineslice. The idea is that the resize
	 * function will be called later, but the assets are set up for loading.
	 * @param	mdo				The data to make the nineslice from
	 */
	public Nineslice(NinesliceMDO mdo) {
		this.mdo = mdo;
		
		filename = Constants.UI_DIR + mdo.file;
		batch = new SpriteBatch();
	}
	
	/**
	 * Creates a nineslice from data, stretched to a certain width/height. The
	 * slicing occurs in the postprocess and the sizing and putting together
	 * happens in an internal call that happens after. This way the actual
	 * final appearance is cached.
	 * @param	mdo				The data to make the nineslice from
	 * @param	width			The initial width of the backer (in window px)
	 * @param	height			The initial height of the backer (in window px)
	 */
	public Nineslice(NinesliceMDO mdo, int width, int height) {
		this(mdo);
		this.width = width;
		this.height = height;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		manager.load(filename, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		tex = manager.get(filename, Texture.class);
		slices = new TextureRegion[3][3];		// indexed y, x
		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1) {
				slices[y][x] = new TextureRegion(
						tex,
						x * mdo.sliceWidth,
						(2-y) * mdo.sliceHeight,
						mdo.sliceWidth,
						mdo.sliceHeight);
			}
		}
		
		if (width > 0 && height > 0) {
			resizeTo(width, height);
		}
	}
	
	/**
	 * Renders the nineslice at a particular location. Should be called by the
	 * owner in their own render call.
	 * @param	batch			The batch to render with (UI batch)
	 * @param	x				The x-coord to render at (in window coords)
	 * @param	y				The y-coord to render at (in window coords)
	 */
	public void renderAt(SpriteBatch batch, float x, float y) {
		batch.begin();
		batch.draw(appearance,
				x, y,
				0, 0,
				width, height);
		batch.end();
	}
	
	/**
	 * Changes the size of the nineslice. This dumps the old texture region
	 * and creates a new one by stitching the others together.
	 * @param	width			The new width to resize to (in window px)
	 * @param	height			The new height to resize to (in window px)
	 */
	public void resizeTo(int width, int height) {
		this.width = width;
		this.height = height;
		
		buffer = new FrameBuffer(Format.RGB565,
				SGlobal.window.getWidth(),
				SGlobal.window.getHeight(),
				false);
		if (appearance != null) {
			appearance.dispose();
		}
		
		buffer.begin();
		batch.begin();
		
		int sw = mdo.sliceWidth;
		int sh = mdo.sliceHeight;
		
		switch (mdo.type) {
		case STRETCH:
			batch.draw(slices[0][1],
					sw, 0,
					width - (sw * 2), sh);
			batch.draw(slices[1][2],
					width - sw, sh,
					sw, height - (sh * 2));
			batch.draw(slices[2][1],
					sw, height - sh,
					width - (sw * 2), sh);
			batch.draw(slices[1][0],
					0, sh,
					sw, height - (sh * 2));
			batch.draw(slices[1][1],
					sw, sh,
					width - (sw * 2), height - (sh * 2));
			break;
		case TILE:
			for (int x = sw; x < width - sw; x += sw) {
				batch.draw(slices[0][1], x, 0);
			}
			for (int y = sh; y < height - sh; y += sh) {
				batch.draw(slices[1][2], width - sw, y);
			}
			for (int x = sw; x < width - sw; x += sw) {
				batch.draw(slices[2][1], x, height - sh);
			}
			for (int y = sh; y < height - sh; y += sh) {
				batch.draw(slices[1][0], 0, y);
			}
			for (int x = sw; x < width - sw; x += sw) {
				for (int y = sh; y < height - sh; y += sh) {
					batch.draw(slices[1][1], 32, 32);
				}
			}
			break;
		}
		
		batch.draw(slices[0][0], 0, 0);
		batch.draw(slices[0][2], width - sw, 0);
		batch.draw(slices[2][2], width - sw, height - sh);
		batch.draw(slices[2][0], 0, height - sh);
		
		batch.end();
		buffer.end();
		appearance = buffer.getColorBufferTexture();
	}

}
