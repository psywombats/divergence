/**
 *  CRoom.java
 *  Created on Oct 25, 2013 6:13:12 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * A room used by cellular generation.
 */
public class CRoom extends Room {
	
	public boolean tensionAvailable;
	public boolean tensionSelected;
	public boolean hallway;
	public boolean added;
	public int cellX, cellY;
	public OrthoDir addside;

	public CRoom(int cellX, int cellY, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.cellX = cellX;
		this.cellY = cellY;
		tensionSelected = false;
		added = false;
		if (width <= 1 || height <= 1) {
			hallway = true;
		}
		tensionAvailable = !hallway;
	}

}
