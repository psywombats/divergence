/**
 *  Room.java
 *  Created on Oct 13, 2013 1:53:26 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.Random;

/**
 * Struct for generation.
 */
public class Room {
	
	public int x, y;
	public int rw, rh;
	
	/**
	 * Creates a new room. A room is just an idea, keep in mind.
	 * @param	x				The x-coord of this room's lower left (in tiles)
	 * @param	y				The y-coord of this room's lower left (in tiles)
	 * @param	width			The width of this room's interior (in tiles)
	 * @param	height			The height of this room's interior (in tiles)
	 */
	public Room(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.rw = width;
		this.rh = height;
	}
	
	public float cx() { return x + (float) rw / 2f; }
	public float cy() { return y + (float) rh / 2f; }
	public int ctx() { return  (int) Math.floor(cx()); }
	public int cty() { return  (int) Math.floor(cy()); }
	
	public int rx(Random r) { return x + r.nextInt(rw); }
	public int ry(Random r) { return y + r.nextInt(rh); }

}
