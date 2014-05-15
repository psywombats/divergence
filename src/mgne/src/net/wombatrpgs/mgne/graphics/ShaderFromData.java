/**
 *  ShaderFromMDO.java
 *  Created on Apr 18, 2013 10:07:54 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;

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
		super(	MGlobal.files.getText(Constants.SHADERS_DIR + mdo.vertexFile),
				MGlobal.files.getText(Constants.SHADERS_DIR + mdo.fragmentFile));
		if (!isCompiled()) {
			MGlobal.reporter.warn("Bad shader:\n" + getLog());
		}
	}
	
	/**
	 * Constructs a new shader from a key to its data.
	 * @param	key				The key to the data to construct from
	 */
	public ShaderFromData(String key) {
		this(MGlobal.data.getEntryFor(key, ShaderMDO.class));
	}

}
