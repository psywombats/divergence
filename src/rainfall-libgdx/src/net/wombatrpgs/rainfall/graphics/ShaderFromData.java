/**
 *  ShaderFromMDO.java
 *  Created on Apr 18, 2013 10:07:54 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.graphics.ShaderMDO;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Loads up shader data from the specified MDO. Unfortunately this thing isn't
 * well-suited to asset management. Just use it.
 */
public class ShaderFromData extends ShaderProgram {

	/**
	 * Constructs a new shader from data.
	 * @param 	mdo				The data to construct from
	 */
	public ShaderFromData(ShaderMDO mdo) {
		super(	RGlobal.loader.getText(Constants.SHADERS_DIR + mdo.vertexFile),
				RGlobal.loader.getText(Constants.SHADERS_DIR + mdo.fragmentFile));
		if (!isCompiled()) {
			RGlobal.reporter.warn("Bad shader:\n" + getLog());
		}
	}

}
