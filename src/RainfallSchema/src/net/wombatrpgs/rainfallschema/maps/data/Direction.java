/**
 *  Dir.java
 *  Created on Nov 12, 2012 11:19:47 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps.data;

/**
 * Basic orthographic directions.
 */
public enum Direction {
	UP			(0, 1),
	RIGHT		(1, 0),
	DOWN		(0, -1),
	LEFT		(-1, 0);
	
	private int x, y;
	
	/**
	 * Creates a new direction with the static components.
	 * @param 	xComp		The x-component of this direction, -1 0 1
	 * @param 	yComp		The y-component of this direction, -1 0 1
	 */
	Direction(int xComp, int yComp) {
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
	
	public static Direction getOpposite(Direction opposite) {
		switch (opposite) {
		case UP: return DOWN;
		case LEFT: return RIGHT;
		case DOWN: return UP;
		case RIGHT: return LEFT;
		default: return null;
		}
	}
}
