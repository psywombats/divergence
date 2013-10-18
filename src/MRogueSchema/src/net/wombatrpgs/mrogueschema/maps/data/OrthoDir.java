/**
 *  Dir.java
 *  Created on Nov 12, 2012 11:19:47 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

/**
 * Basic orthographic directions.
 */
public enum OrthoDir {
	NORTH			(0, 1),
	EAST			(1, 0),
	SOUTH			(0, -1),
	WEST			(-1, 0);
	
	private int x, y;
	
	/**
	 * Creates a new direction with the static components.
	 * @param 	xComp		The x-component of this direction, -1 0 1
	 * @param 	yComp		The y-component of this direction, -1 0 1
	 */
	OrthoDir(int xComp, int yComp) {
		this.x = xComp;
		this.y = yComp;
	}
	
	/**
	 * Returns the unit vector associated with this direction
	 * @return				The unit vector, in arbitrary units (duh)
	 */
	public DirVector getVector() {
		return new DirVector(x, y);
	}
	
	public static OrthoDir getOpposite(OrthoDir opposite) {
		switch (opposite) {
		case NORTH: return SOUTH;
		case WEST: return EAST;
		case SOUTH: return NORTH;
		case EAST: return WEST;
		default: return null;
		}
	}

	public EightDir toEight() {
		switch (this) {
		case NORTH: return EightDir.NORTH;
		case EAST: return EightDir.EAST;
		case SOUTH: return EightDir.SOUTH;
		case WEST: return EightDir.WEST;
		default: return null;
		}
	}
}
