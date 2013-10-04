/**
 *  DataEntry.java
 *  Created on Feb 4, 2013 5:56:47 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

import net.wombatrpgs.mgns.core.MainSchema;

import com.badlogic.gdx.files.FileHandle;

/**
 * A single entry from the data folder. Basically a wrapped for a string. But it
 * inserts itself into the database on load! Isn't that cool? No processing!
 */
public class DataEntry {
	
	protected Class<? extends MainSchema> clazz;
	protected String data;
	
	/**
	 * Creates a new data entry by loading from a json file
	 * @param 	handle			The .json file to load from
	 */
	public DataEntry(FileHandle handle) {
		data = handle.readString();
		MGlobal.data.parseString(data, getSchemaByFile(handle));
	}
	
	/** @return The string blob (really json) that made up the data file */
	public String getData() { return data; }
	
	/**
	 * Gets the actual schema for a data object based on its file location.
	 * @param 		dataFile 	The data file to get the schema for
	 * @return 					The class of schema of the data
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends MainSchema> getSchemaByFile(FileHandle dataFile) {
		String ourPath = dataFile.path();
		String className = ourPath.substring(Constants.DATA_DIR.length(), ourPath.length());
		className = className.replace('\\', '/');
		if (className.startsWith("/")) className = className.substring(1);
		if (!dataFile.isDirectory()) {
			className = className.substring(0, className.lastIndexOf('/'));
		}
		className = className.replace('/', '.');
		try {
			// we do forName here instead of classloading because we can assumed
			// that this project includes its games' scheme files
			// If not something else will need to be done...
			Class<?> rawClass = Class.forName(className);
			if (!MainSchema.class.isAssignableFrom(rawClass)) {
				MGlobal.reporter.warn("Loaded class " + rawClass + " didn't extend base schema");
			}
			return (Class<? extends MainSchema>) rawClass;
		} catch (ClassNotFoundException e) {
			MGlobal.reporter.err("Couldn't find a class " + className, e);
			return null;
		}
	}

}
