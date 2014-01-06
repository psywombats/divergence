/**
 *  TileType.java
 *  Created on Oct 5, 2013 2:38:39 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

/**
 * The possible roles of a tile, such as wall, ceiling, floor, etc. Dictates
 * passability and things like that.
 */
public enum TileType {
	
	FLOOR			(true,	true),
	OBSTACLE		(false, true),
	USTAIR_TOP		(false,	true),
	USTAIR_BOTTOM	(true,	true),
	DSTAIR_TOP		(true,	true),
	DSTAIR_BOTTOM	(false,	true),
	ANY_CEILING		(false, false),
	CEILING_TOP		(false, false),
	CEILING_BOTTOM	(false, false),
	CEILING_MIDDLE	(false, false),
	ANY_MIDDLE_WALL	(false, true),			// for MDO use only
	ANY_WALL		(false, true),			// for MDO use only
	WALL_TLEFT		(false, true),
	WALL_BLEFT		(false, true),
	WALL_TOP		(false,	true),
	WALL_BOTTOM		(false, true),
	WALL_TRIGHT		(false, true),
	WALL_BRIGHT		(false, true),
	WALL_TMID		(false, true),
	WALL_BMID		(false, true);
	
	private boolean passable;
	private boolean transparent;
	
	TileType(boolean passable, boolean transparent) {
		this.passable = passable;
		this.transparent = transparent;
	}
	
	/** @return True if this tile can be stepped on, false otherwise */
	public boolean isPassable() { return passable; }
	
	/** @return True if this tile is see-through */
	public boolean isTransparent() { return transparent; }

}
