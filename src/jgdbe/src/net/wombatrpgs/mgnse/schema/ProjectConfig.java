/**
 *  Config.java
 *  Created on Aug 6, 2012 2:35:03 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.schema;

/**
 * Configuration for a .mgndb file. Basically it's a json POJO.
 */
public class ProjectConfig {

	/** Project name */
	public String name;
	/** Data directory for the database */
	public String data;
	/** Schema directory */
	public String schema;
	
}
