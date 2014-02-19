/**
 *  DirEnum.java
 *  Created on Feb 14, 2014 1:53:51 AM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.maps.data;

/**
 * All enums that represent directions.
 */
public interface DirEnum {
	
	/**
	 * Returns the unit vector associated with this direction
	 * @return				The unit vector, with 1 as its components
	 */
	public DirVector getVector();

}
