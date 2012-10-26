/**
 *  ContextualFileLoader.java
 *  Created on Oct 10, 2012 11:47:09 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.io;

import java.io.File;

/**
 * An interface for loading a file in a particular project context. It basically
 * maps a few InputHandler methods with some arguments so that they don't have
 * to get passed down all the time.
 */
public interface ContextualFileLoader {
	
	/**
	 * Loads a file from this context.
	 * @param name		The name of the file
	 * @return			The file or directory named, from the project folder
	 */
	public File getFile(String name);

}
