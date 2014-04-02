/**
 *  PolymorphicLink.java
 *  Created on Apr 2, 2014 12:21:11 AM for project jgdb-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgns.core;

/**
 * A combined type/key link
 */
public class PolymorphicLink {
	
	/** The type of schema this link is to, canonical name */
	public String clazz;
	
	/** The unique key of that schema */
	public String key;

}
