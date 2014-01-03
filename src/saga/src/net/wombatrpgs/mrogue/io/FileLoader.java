/**
 *  FileLoader.java
 *  Created on Apr 18, 2013 7:18:53 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Just a thing that sits in RGlobal for easy text file access. Don't load
 * anything expensive with these; they're really only intended to be used for
 * simple config files and shaders.
 */
public class FileLoader {
	
	/**
	 * Loads up a file then reads it as a string. Processes the file as being
	 * internal.
	 * @param 	fileName			The name of the file to load, relative to
	 * 								the game directory, ie "res/shaders/a.txt"
	 * @return						That file's contents as a string
	 */
	public String getText(String fileName) {
		FileHandle handle = Gdx.files.internal(fileName);
		String result = handle.readString();
		return result;
	}

}
