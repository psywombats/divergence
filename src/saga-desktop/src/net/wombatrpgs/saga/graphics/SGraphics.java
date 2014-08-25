/**
 *  SagaGraphics.java
 *  Created on May 15, 2014 1:14:51 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics;

import java.util.Scanner;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.BatchWithShader;
import net.wombatrpgs.mgne.graphics.GraphicsSettings;
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.sagaschema.graphics.PaletteMDO;
import net.wombatrpgs.sagaschema.graphics.SagaGraphicsMDO;

/**
 * SaGa-style graphics settings.
 */
public class SGraphics extends GraphicsSettings {
	
	protected SagaGraphicsMDO mdo;
	
	protected FontHolder numFont;
	protected float[] white, black;
	
	/**
	 * Creates a new saga graphics from some data.
	 * @param	mdo				The data to generate from
	 */
	public SGraphics(SagaGraphicsMDO mdo) {
		this.mdo = mdo;
		
		white = new float[3];
		black = new float[3];
		PaletteMDO bg = MGlobal.data.getEntryFor(mdo.bgPalette, PaletteMDO.class);
		Vector3 cWhite = parseCString(bg.outWhite);
		Vector3 cBlack = parseCString(bg.outBlack);
		white[0] = cWhite.x;
		white[1] = cWhite.y;
		white[2] = cWhite.z;
		black[0] = cBlack.x;
		black[1] = cBlack.y;
		black[2] = cBlack.z;
		
		numFont = new FontHolder(mdo.font);
		assets.add(numFont);
	}
	
	/**
	 * Creates a new saga graphics from the key to some data.
	 * @param	key				The key to some data to use
	 */
	public SGraphics(String key) {
		this(MGlobal.data.getEntryFor(key, SagaGraphicsMDO.class));
	}
	
	/**
	 * Creates a new saga graphics from the default key.
	 */
	public SGraphics() {
		this(SGlobal.settings.getGraphicsKey());
	}
	
	/**
	 * Constructs a new batch that uses the gameboy background filter.
	 * @return					The batch using the gameboy filter
	 */
	public BatchWithShader constructBackgroundBatch() {
		return constructGameboyBatch(mdo.bgPalette);
	}
	
	/**
	 * Constructs a new batch that uses the gameboy foreground filter.
	 * @return					The batch using the gameboy filter
	 */
	public BatchWithShader constructForegroundBatch() {
		return constructGameboyBatch(mdo.fgPalette);
	}
	
	/**
	 * Returns the stored array of the background white wipe color.
	 * @return					The background color of the screen in float[]
	 */
	public float[] getWhite() {
		return white;
	}
	
	/**
	 * Returns the stored array of the background white wipe color.
	 * @return					The background color of the screen in float[]
	 */
	public float[] getBlack() {
		return black;
	}
	
	/**
	 * Returns the set shader data for screen wipe transitions.
	 * @return					The shader data for wipes
	 */
	public ShaderMDO getWipeShaderMDO() {
		return MGlobal.data.getEntryFor(mdo.wipeShader, ShaderMDO.class);
	}
	
	/**
	 * Returns the font for rendering battle popup numbers.
	 * @return					The number popup font
	 */
	public FontHolder getNumFont() {
		return numFont;
	}
	
	/**
	 * Constructs a new batch that uses the gameboy filter. Will use the default
	 * filter palette to parse colors.
	 * @param	paletteKey		The database key of the output palette
	 * @return					The batch using the gameboy filter
	 */
	protected BatchWithShader constructGameboyBatch(String paletteKey) {
		SpriteBatch batch = MGlobal.graphics.constructBatch();
		ShaderFromData shader =  MGlobal.graphics.constructShader(mdo.shader);
		PaletteMDO bg = MGlobal.data.getEntryFor(paletteKey, PaletteMDO.class);
		
		if (MGlobal.graphics.isShaderEnabled() && shader != null) {
			batch.setShader(shader);
			batch.begin();
			
			shader.setUniformf("u_black", parseCString(mdo.filterBlack));
			shader.setUniformf("u_dgray", parseCString(mdo.filterDgray));
			shader.setUniformf("u_lgray", parseCString(mdo.filterLgray));
			shader.setUniformf("u_white", parseCString(mdo.filterWhite));
			
			shader.setUniformf("u_blackOut", parseCString(bg.outBlack));
			shader.setUniformf("u_dgrayOut", parseCString(bg.outDgray));
			shader.setUniformf("u_lgrayOut", parseCString(bg.outLgray));
			shader.setUniformf("u_whiteOut", parseCString(bg.outWhite));
			
			shader.setUniformf("u_height", MGlobal.window.getHeight());
			
			batch.end();
		}
		
		return new BatchWithShader(batch, shader);
	}
	
	/**
	 * Parses a string in the form rrr,ggg,bbb to a 3vec.
	 * @param	str				The rrr,ggg,bbb string to parse
	 * @return					That string in a float array format
	 */
	protected Vector3 parseCString(String str) {
		Scanner sc = new Scanner(str);
		sc.useDelimiter(",");
		float[] vec = new float[3];
		for (int i = 0; i < 3; i += 1) {
			String val = sc.next();
			vec[i] = Float.valueOf(val) / 256f;
		}
		sc.close();
		return new Vector3(vec);
	}

}
