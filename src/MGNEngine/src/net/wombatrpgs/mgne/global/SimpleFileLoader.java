/**
 *  FileLoader.java
 *  Created on Nov 4, 2012 5:48:23 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Generic loader thing to get resources from within the game's jar. It's a
 * little low-level, so something's probably supposed to wrap this so that
 * loading all the objects at game loadtime isn't horrible.
 */
public class SimpleFileLoader {
	
	/**
	 * Returns the resource stream associated with the named file in our own
	 * jar.
	 * @param 	path	The path to the file (ie "res/filez.txt")
	 * @return			That file as a stream
	 */
	public InputStream getStream(String path) {
		try {
			InputStream stream = new FileInputStream(path);
			return stream;
		} catch (FileNotFoundException e) {
			Global.reporter.err("Couldn't find " + path, e);
			return null;
		}
	}
	
	/**
	 * Returns the resource stream associated with the named file in our own
	 * jar, primed for file i/o.
	 * @param	path	The path to the file (ie "res/filez.txt")
	 * @return			That path as a file stream
	 */
	public FileInputStream getFileStream(String path) {
		try {
			FileInputStream stream = new FileInputStream(path);
			return stream;
		} catch (FileNotFoundException e) {
			Global.reporter.err("Couldn't find " + path, e);
			return null;
		}
	}

}
