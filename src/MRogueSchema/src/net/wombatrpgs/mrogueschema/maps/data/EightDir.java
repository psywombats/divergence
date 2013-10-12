/**
 *  Dir.java
 *  Created on Nov 12, 2012 11:19:47 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

import java.util.Random;

/**
 * Basic diagonal ortho directions.
 */
public enum EightDir {
	NORTH			(0, 1),
	NORTHEAST		(1, 1),
	EAST			(1, 0),
	SOUTHEAST		(1, -1),
	SOUTH			(0, -1),
	SOUTHWEST		(-1, -1),
	WEST			(-1, 0),
	NORTHWEST		(-1, 1);
	
	private int x, y;
	
	/**
	 * Creates a new direction with the static components.
	 * @param 	xComp		The x-component of this direction, -1 0 1
	 * @param 	yComp		The y-component of this direction, -1 0 1
	 */
	EightDir(int xComp, int yComp) {
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
	
	/**
	 * Returns the orthographic equivalent of this direction. In case of a
	 * specific compass direction, decides randomly.
	 * @param	r				The RNG to decide with
	 * @return					The ortho equivalent of this direction
	 */
	public OrthoDir toOrtho(Random r) {
		switch(this) {
		case NORTH:			return OrthoDir.NORTH;
		case NORTHEAST:		return r.nextBoolean() ? OrthoDir.NORTH : OrthoDir.EAST;
		case EAST:			return OrthoDir.EAST;
		case SOUTHEAST:		return r.nextBoolean() ? OrthoDir.EAST : OrthoDir.SOUTH;
		case SOUTH:			return OrthoDir.SOUTH;
		case SOUTHWEST:		return r.nextBoolean() ? OrthoDir.SOUTH : OrthoDir.WEST;
		case WEST:			return OrthoDir.WEST;
		case NORTHWEST:		return r.nextBoolean() ? OrthoDir.WEST : OrthoDir.NORTH;
		default:			return null;
		}
	}
	
	/**
	 * Returns the orthographic equivalent of this direction. In case of a
	 * specific compass direction, leans towards a specified preference dir.
	 * @param	d				The preferred tiebreaker
	 * @return					The ortho equivalent of this direction
	 */
	public OrthoDir toOrtho(OrthoDir d) {
		switch(this) {
		case NORTH:			return OrthoDir.NORTH;
		case NORTHEAST:		return d==OrthoDir.NORTH ? OrthoDir.NORTH : OrthoDir.EAST;
		case EAST:			return OrthoDir.EAST;
		case SOUTHEAST:		return d==OrthoDir.EAST ? OrthoDir.EAST : OrthoDir.SOUTH;
		case SOUTH:			return OrthoDir.SOUTH;
		case SOUTHWEST:		return d==OrthoDir.SOUTH ? OrthoDir.SOUTH : OrthoDir.WEST;
		case WEST:			return OrthoDir.WEST;
		case NORTHWEST:		return d==OrthoDir.WEST ? OrthoDir.WEST : OrthoDir.NORTH;
		default:			return null;
		}
	}
	
	/**
	 * Returns the orthographic equivalent of this direction. In case of a
	 * specific compass direction, decides arbitrarily.
	 * @return					The ortho equivalent of this direction
	 */
	public OrthoDir toOrtho() {
		switch(this) {
		case NORTH:			return OrthoDir.NORTH;
		case NORTHEAST:		return OrthoDir.NORTH;
		case EAST:			return OrthoDir.EAST;
		case SOUTHEAST:		return OrthoDir.EAST;
		case SOUTH:			return OrthoDir.SOUTH;
		case SOUTHWEST:		return OrthoDir.SOUTH;
		case WEST:			return OrthoDir.WEST;
		case NORTHWEST:		return OrthoDir.WEST;
		default:			return null;
		}
	}
	
	public static EightDir getOpposite(EightDir opposite) {
		switch (opposite) {
		case NORTH: return SOUTH;
		case WEST: return EAST;
		case SOUTH: return NORTH;
		case EAST: return WEST;
		case NORTHWEST: return SOUTHEAST;
		case SOUTHWEST: return NORTHEAST;
		case SOUTHEAST: return NORTHWEST;
		case NORTHEAST: return SOUTHWEST;
		default: return null;
		}
	}

}
