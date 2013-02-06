/**
 *  DirVector.java
 *  Created on Nov 25, 2012 7:09:52 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps.data;

/**
 * Struct to keep track of velocities. Represents a velocity or acceleration
 * vector, not a point. Vector is overloaded enough already!
 */
public class DirVector {
	
	/** x-component, usually in pixels */
	public int x;
	/** y-component, usually in pixels */
	public int y;
	
	/**
	 * Creates a new struct with the specified values.
	 * @param 	x			x-component, usually in pixels
	 * @param 	y			y-component, usually in pixels
	 */
	public DirVector(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
