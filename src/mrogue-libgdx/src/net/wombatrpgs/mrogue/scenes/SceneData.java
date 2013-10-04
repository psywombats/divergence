/**
 *  SceneData.java
 *  Created on Feb 3, 2013 11:33:20 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;

/**
 * Object thing that stores scene data. It's essentially a container for an
 * array of strings that are loaded from disc.
 */
public class SceneData {
	
	protected static final String SPLITTER = "\n";
	
	protected List<String> lines;
	
	/**
	 * Creates a new scene from a file handle.
	 * @param 	file			The file handle to load from
	 */
	public SceneData(FileHandle file) {
		String full = file.readString();
		full = full.replaceAll("\\r\\n", "\n");
		full = full.replaceAll("\\r", "\n");
		lines = new ArrayList<String>();
		while (full.indexOf(SPLITTER) != -1) {
			lines.add(full.substring(0, full.indexOf(SPLITTER)));
			full = full.substring(full.indexOf(SPLITTER) + 1);
		}
		lines.add(full);
	}
	
	/** @return All the strings that made up the scene */
	public List<String> getLines() {
		return lines;
	}

}
