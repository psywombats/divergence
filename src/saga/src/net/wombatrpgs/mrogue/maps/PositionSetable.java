/**
 *  PositionSetable.java
 *  Created on Nov 29, 2012 2:06:20 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

/**
 * Not only can implementing objects have a position evaluated, they can have
 * their position publicly moved around.
 */
public interface PositionSetable extends Positionable {
	
	/**
	 * Sets the x-coord of the implementing object.
	 * @param 	x			The new x-coord, in pixels
	 */
	public void setX(float x);
	
	/**
	 * Sets the y-coord of the implementing object.
	 * @param 	y			The new y-coord, in pixels
	 */
	public void setY(float y);


}
