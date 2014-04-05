/**
 *  Boundable.java
 *  Created on Apr 5, 2014 2:48:45 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics.interfaces;

/**
 * This means you can find the width and height of the represented object.
 */
public interface Boundable {
	
	/**
	 * Calculates or retrieves the width of the renderable represented. Results
	 * usually in virtual px.
	 * @return					The width of the object.
	 */
	public int getWidth();
	
	/**
	 * Calculates or retrieves the height of the renderable represented. Results
	 * usually in virtual px.
	 * @return					The height of the object
	 */
	public int getHeight();

}
