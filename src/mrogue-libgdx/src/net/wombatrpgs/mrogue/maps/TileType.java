/**
 *  TileType.java
 *  Created on Oct 5, 2013 2:38:39 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

/**
 * The possible roles of a tile, such as wall, ceiling, floor, etc. Dictates
 * passability and things like that.
 */
public enum TileType {
	
	FLOOR		(true),
	WALL		(false);
	
	private boolean passable;
	TileType(boolean passable) {
		this.passable = passable;
	}
	
	/** @return True if this tile can be stepped on, false otherwise */
	public boolean isPassable() { return passable; }

}
