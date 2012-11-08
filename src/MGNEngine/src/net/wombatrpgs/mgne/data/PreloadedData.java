/**
 *  PreloadedData.java
 *  Created on Nov 4, 2012 8:07:05 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A struct for pre-processed JSON.
 */
public class PreloadedData {
	
	/** The specific class of the schema */
	Class<? extends MainSchema> clazz;
	/** The JSON string comprising this data */
	String data;
	
	/**
	 * Constructs a new data struct.
	 * @param clazz		The specific class of the schema
	 * @param data		The JSON string comprising this data
	 */
	public PreloadedData(Class<? extends MainSchema> clazz, String data) {
		this.clazz = clazz;
		this.data = data;
	}

}
