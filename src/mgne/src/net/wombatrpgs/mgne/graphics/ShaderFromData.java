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
	 * Constructs a new shader from two filenames.
	 * @param	vertexFile		The vertex filename, relative to shaders dir
	 * @param	fragFile		The fragment filename, relative to shaders dir
	 */
	public ShaderFromData(String vertexFile, String fragFile) {
		super(	MGlobal.files.getText(Constants.SHADERS_DIR + vertexFile),
				MGlobal.files.getText(Constants.SHADERS_DIR + fragFile));
		if (!isCompiled()) {
			MGlobal.reporter.warn("Bad shader:\n" + getLog());
		}
	}

	/**
	 * Constructs a new shader from data.
	 * @param 	mdo				The data to construct from
	 */
	public ShaderFromData(ShaderMDO mdo) {
		this(mdo.vertexFile, mdo.fragmentFile);
	}
	
	/**
	 * Constructs a new shader from a key to its data.
	 * @param	key				The key to the data to construct from
	 */
	public ShaderFromData(String key) {
		this(MGlobal.data.getEntryFor(key, ShaderMDO.class));
	}

}
