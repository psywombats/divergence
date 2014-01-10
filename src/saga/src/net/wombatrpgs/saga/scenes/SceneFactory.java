/**
 *  SceneFactory.java
 *  Created on Oct 22, 2013 1:31:43 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.screen.Screen;
import net.wombatrpgs.sagaschema.cutscene.SceneMDO;
import net.wombatrpgs.sagaschema.cutscene.data.SceneParentMDO;

/**
 * Converts scene MDO into full-fledged scenes. This is not a singleton because
 * creation has states, ie, for exclusive selection.
 */
public class SceneFactory {
	
	// from set mdo key to list of file names
	protected Map<String, List<String>> sets;
	
	/**
	 * Creates a blank scene factory. Will select only scenes that this
	 * factory has no created before when handed a set.
	 */
	public SceneFactory() {
		sets = new HashMap<String, List<String>>();
	}
	
	/**
	 * Creates the appropriately subclassed scene parser.
	 * @param	mdo				The data to create from
	 * @param	parent			The screen to create with, if necessary
	 * @return					The scene parser from data
	 */
	public SceneParser createScene(SceneParentMDO mdo, Screen parent) {
		if (SceneMDO.class.isAssignableFrom(mdo.getClass())) {
			// explicit, fine
			return new SceneParser((SceneMDO) mdo, parent);
		} else {
			SGlobal.reporter.err("A scene parsing doesn't work");
			return null;
		}
	}

}
