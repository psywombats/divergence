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
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.sagaschema.graphics.SagaGraphicsMDO;

/**
 * SaGa-style graphics settings.
 */
public class SagaGraphics {
	
	protected SagaGraphicsMDO mdo;
	
	/**
	 * Creates a new saga graphics from some data.
	 * @param	mdo				The data to generate from
	 */
	public SagaGraphics(SagaGraphicsMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Creates a new saga graphics from the key to some data.
	 * @param	key				The key to some data to use
	 */
	public SagaGraphics(String key) {
		this(MGlobal.data.getEntryFor(key, SagaGraphicsMDO.class));
	}
	
	/**
	 * Creates a new saga graphics from the default key.
	 */
	public SagaGraphics() {
		this(SGlobal.settings.getGraphicsKey());
	}
	
	/**
	 * Constructs a new batch that uses the gameboy filter. Called by screen.
	 * @return					The batch using the gameboy filter
	 */
	public BatchWithShader constructGameboyBatch() {
		SpriteBatch batch = MGlobal.graphics.constructBatch();
		ShaderFromData shader =  MGlobal.graphics.constructGlobalShader();
		
		if (MGlobal.graphics.isShaderEnabled() && shader != null) {
			batch.setShader(shader);
			batch.begin();
			
			shader.setUniformf("u_black", parseCString(mdo.filterBlack));
			shader.setUniformf("u_dgray", parseCString(mdo.filterDgray));
			shader.setUniformf("u_lgray", parseCString(mdo.filterLgray));
			shader.setUniformf("u_white", parseCString(mdo.filterWhite));
			
			shader.setUniformf("u_blackOut", parseCString(mdo.outBlack));
			shader.setUniformf("u_dgrayOut", parseCString(mdo.outDgray));
			shader.setUniformf("u_lgrayOut", parseCString(mdo.outLgray));
			shader.setUniformf("u_whiteOut", parseCString(mdo.outWhite));
			
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
