/**
 *  VersionInfo.java
 *  Created on Nov 23, 2014 7:19:17 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

/**
 * Struct used for error reporting, mostly.
 */
public class VersionInfo {
	
	protected static final String DEFAULT_USER_NAME = "anonymous";
	
	public String version;
	public int build;
	public String gameName;
	
	/**
	 * Creates a new struct with version information.
	 * @param	version			The version string of this build
	 * @param	build			The number of this build
	 * @param	gameName		The name of the game
	 */
	public VersionInfo(String version, int build, String gameName) {
		this.version = version;
		this.build = build;
		this.gameName = gameName;
	}
	
	/**
	 * If the game has a login or identifier system, use it here.
	 * @return					The name of the user playing the game now
	 */
	public String getUserName() {
		return DEFAULT_USER_NAME;
	}

}
