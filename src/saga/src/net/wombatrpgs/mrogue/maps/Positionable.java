/**
 *  Positionable.java
 *  Created on Nov 14, 2012 11:32:35 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

/**
 * Any object that can be positioned on the map and rendered at a location.
 */
public interface Positionable {
	
	/** @return The x-coord (in screen space pixels) of this object */
	public float getX();
	
	/** @return The y-coord (in screen space pixels) of this object */
	public float getY();

}
