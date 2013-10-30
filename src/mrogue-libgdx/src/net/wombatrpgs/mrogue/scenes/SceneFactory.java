/**
 *  SceneFactory.java
 *  Created on Oct 22, 2013 1:31:43 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.cutscene.CharacterSetMDO;
import net.wombatrpgs.mrogueschema.cutscene.SceneMDO;
import net.wombatrpgs.mrogueschema.cutscene.SceneSetMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.HeadlessSceneMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;

/**
 * Converts scene MDO into full-fledged scenes. This is not a singleton because
 * creation has states, ie, for exclusive selection.
 */
public class SceneFactory {
	
	// from set mdo key to list of file names
	protected Map<String, List<String>> sets;
	// from charas mdo to charas
	protected Map<String, CharacterSet> charas;
	
	/**
	 * Creates a blank scene factory. Will select only scenes that this
	 * factory has no created before when handed a set.
	 */
	public SceneFactory() {
		sets = new HashMap<String, List<String>>();
		charas = new HashMap<String, CharacterSet>();
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
		} else if (SceneSetMDO.class.isAssignableFrom(mdo.getClass())) {
			if (parent == null) {
				MGlobal.reporter.err("No screen passed, but mdo is list");
				return null;
			}
			SceneSetMDO setMDO = (SceneSetMDO) mdo;
			if (!sets.containsKey(setMDO.key)) {
				List<String> files = new ArrayList<String>();
				for (HeadlessSceneMDO headless : setMDO.scenes) {
					files.add(headless.file);
				}
				sets.put(setMDO.key, files);
			}
			if (!charas.containsKey(setMDO.charas)) {
				CharacterSetMDO charasMDO = MGlobal.data.getEntryFor(setMDO.charas, CharacterSetMDO.class);
				charas.put(setMDO.charas, new CharacterSet(charasMDO));
			}
			List<String> files = sets.get(setMDO.key);
			if (files.size() == 0) {
				MGlobal.reporter.err("No more scenes availble.");
				return null;
			}
			int index = MGlobal.rand.nextInt(files.size());
			String file = files.get(index);
			files.remove(index);
			return new SceneParser(file, parent, charas.get(setMDO.charas));
		} else {
			MGlobal.reporter.err("Unknown parser mdo: " + mdo);
			return null;
		}
	}
	
	/**
	 * Awful thing that I hate. Why is this here?
	 * @return
	 */
	public String getHeroName() {
		return charas.get(charas.keySet().toArray()[0]).toName("HERO");
	}
	
	/**
	 * Why am I doing this again?
	 * @return
	 */
	public String getBossName() {
		return charas.get(charas.keySet().toArray()[0]).toName("EVIL");
	}

}
