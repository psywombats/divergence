/**
 *  Path.java
 *  Created on Mar 14, 2013 7:07:50 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * A partial path to a destination on a map. Only stores destination, not
 * obstacle data. Sorts itself by a birds-eye heuristic.
 */
public class Path implements Comparable<Path> {
	
	/** How to get from the starting point to where we are now */
	protected List<EightDir> steps;
	
	/** Where we're aiming for, in tiles */
	protected int destX, destY;
	
	/** Where we're located, in tiles */
	protected int atX, atY;
	
	/**
	 * Creates a path that starts somewhere and aims somewhere else. Used for
	 * the first in a search.
	 * @param 	destX			Where the search is going x in tiles
	 * @param 	destY			Where the search is going y in tiles
	 * @param 	atX			Where the search originates x in tiles
	 * @param 	atY			Where the search originates y in tiles
	 */
	public Path(int destX, int destY, int atX, int atY) {
		this.steps = new ArrayList<EightDir>();
		this.destX = destX;
		this.destY = destY;
		this.atX = atX;
		this.atY = atY;
	}
	
	/**
	 * Constructs a new path as a followup from another path.
	 * @param 	parent			The set of moves that led to the parent path
	 * @param 	step			The step that this path moves, its "fork"
	 * @param 	destX			The destination of the pathfinding x in tiles
	 * @param 	destY			The destination of the pathfinding y in tiles
	 * @param 	fromX			Parent's current location x in tiles
	 * @param 	fromY			Parent's current location y in tiles
	 */
	public Path(List<EightDir> parent, EightDir step, 
			int destX, int destY, int fromX, int fromY) {
		this(destX, destY, (int) (fromX + step.getVector().x),  (int) (fromY + step.getVector().y));
		this.steps.addAll(parent);
		this.steps.add(step);
	}
	
	/**
	 * A simplified from-parent constructor that infers info from parent path.
	 * @param 	parent			The path that spawned this one
	 * @param 	step			The direction that this path forks
	 */
	public Path(Path parent, EightDir step) {
		this(parent.steps, step, parent.destX, parent.destY, parent.atX, parent.atY);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Path other) {
		float val = this.heuristic() - other.heuristic();
		if (val > 0) return (int) Math.ceil(val);
		if (val < 0) return (int) Math.floor(val);
		return (int) val;
	}
	
	/** @return The steps to take to follow the path */
	public List<EightDir> getSteps() {
		return steps;
	}
	
	/** @return Where node is located x-coord in tiles */
	public int getAtX() { return atX; }
	
	/** @return Where node is located x-coord in tiles */
	public int getAtY() { return atY; }
	
	/**
	 * Calculates the "score" of this path. In this case, it's traveled
	 * distance plus estimated distance to go.
	 * @return
	 */
	protected float heuristic() {
		float dx = destX - atX;
		float dy = destY - atY;
		float dist = (float) Math.sqrt(dx*dx + dy*dy);
		return steps.size() + dist;
	}

}
