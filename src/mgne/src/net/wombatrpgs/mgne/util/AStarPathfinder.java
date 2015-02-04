/**
 *  AStarPathfinder.java
 *  Created on Mar 14, 2013 7:23:18 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;

import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.maps.data.DirVector;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * Pathfinds towards a destination. Ooooh, spooky! It uses black magic to work
 * out a path. Actually it just uses heuristic search algorithms but w/e. This
 * is relatively expensive and if you call it every frame you're somewhat of an
 * idiot. (Actually it shouldn't be too bad because this isn't android) If there
 * are ever more ways of finding a path, this thing should implement the
 * Pathfinder interface.
 */
public class AStarPathfinder {
	
	protected Level map;
	protected int fromX, fromY;
	protected int toX, toY;
	
	/**
	 * Creates a new shell of a pathfinder. This can be used for a reusable
	 * pathfinder, but make sure you set the fields first.
	 */
	public AStarPathfinder() {
		
	}
	
	/**
	 * Sets up a new pathfinding task on a level. Does not actually evaluate
	 * anything unless you tell it to. You can probably reuse it by changing
	 * around its to/from. That should help for android optimizations if it
	 * ever comes to that.
	 * @param 	map				The map that this task runs on
	 * @param 	fromX			Where search starts from x (in tiles)
	 * @param 	fromY			Where search starts from y (in tiles)
	 * @param 	toX				Where search starts from x (in tiles)
	 * @param 	toY				Where search starts from y (in tiles)
	 */
	public AStarPathfinder(Level map, int fromX, int fromY, int toX, int toY) {
		this();
		setInfo(map, fromX, fromY, toX, toY);
	}
	
	/**
	 * Sets the tracking information for the pathfinding.
	 * @param 	map				The map that this task runs on
	 * @param 	fromX			Where search starts from x (in tiles)
	 * @param 	fromY			Where search starts from y (in tiles)
	 * @param 	toX				Where search starts from x (in tiles)
	 * @param 	toY				Where search starts from y (in tiles)
	 */
	public void setInfo(Level map, int fromX, int fromY, int toX, int toY) {
		this.map = map;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	
	/**
	 * Sets where this search is headed.
	 * @param 	toX				Where search is headed x (in tiles)
	 * @param 	toY				Where search is headed y (in tiles)
	 */
	public void setTarget(int toX, int toY) {
		this.toX = toX;
		this.toY = toY;
	}
	
	/**
	 * Sets where this search is from.
	 * @param 	fromX			Where search is from x (in tiles)
	 * @param 	fromY			Where search is from y (in tiles)
	 */
	public void setStart(int fromX, int fromY) {
		this.fromX = fromX;
		this.fromY = fromY;
	}
	
	/**
	 * Sets the map the search takes place on.
	 * @param	map				The map this search takes place on
	 */
	public void setMap(Level map) {
		this.map = map;
	}
	
//	/**
//	 * Finds the path for an actor using all 8 directions.
//	 * @return					The resulting path, or null if none
//	 */
//	public List<EightDir> getEightPath() {
//		return getPath(EightDir.values());
//	}
	
	/**
	 * Finds the path for an actor using cardinal directions.
	 * @return					The resulting path, or null if none
	 */
	public List<OrthoDir> getOrthoPath() {
		Queue<Path<OrthoDir>> queue = new PriorityQueue<Path<OrthoDir>>();
		queue.add(new Path<OrthoDir>(toX, toY, fromX, fromY));
		// I can't believe I'm making a 2D array like this
		List<Vector2> visited = new ArrayList<Vector2>();
		
		while (queue.size() > 0) {
			Path<OrthoDir> node = queue.poll();
			if (visited.contains(new Vector2(node.getAtX(), node.getAtY()))) {
				// we've already been here
				continue;
			} else {
				visited.add(new Vector2(node.getAtX(), node.getAtY()));
				if (node.getAtX() == toX && node.getAtY() == toY) {
					//MGlobal.reporter.inform("Path found, expanded " + nodes);
					return node.getSteps();
				}
				List<OrthoDir> dirSet = new ArrayList<OrthoDir>();
				dirSet.add(dirTo(node.getAtX(), node.getAtY(), toX, toY));
				dirSet.addAll(Arrays.asList(OrthoDir.values()));
				for (OrthoDir dir : dirSet) {
					DirVector vec = dir.getVector();
					int nextX = (int) (vec.x + node.getAtX());
					int nextY = (int) (vec.y + node.getAtY());
					if (nextX >= 0 && nextX < map.getWidth() &&
						nextY >= 0 && nextY < map.getHeight() &&
						!visited.contains(new Vector2(nextX, nextY)) &&
						map.isChipPassable(nextX, nextY)) {
						
						queue.add(new Path<OrthoDir>(node, dir));
					}
				}
			}
		}
		return null;
	}
	
	public List<OrthoDir> getBigOrthoPath(int size) {
		Queue<Path<OrthoDir>> queue = new PriorityQueue<Path<OrthoDir>>();
		queue.add(new Path<OrthoDir>(toX, toY, fromX, fromY));
		// I can't believe I'm making a 2D array like this
		List<Vector2> visited = new ArrayList<Vector2>();
		
		while (queue.size() > 0) {
			Path<OrthoDir> node = queue.poll();
			if (visited.contains(new Vector2(node.getAtX(), node.getAtY()))) {
				// we've already been here
				continue;
			} else {
				visited.add(new Vector2(node.getAtX(), node.getAtY()));
				if (node.getAtX() == toX && node.getAtY() == toY) {
					//MGlobal.reporter.inform("Path found, expanded " + nodes);
					return node.getSteps();
				}
				List<OrthoDir> dirSet = new ArrayList<OrthoDir>();
				dirSet.add(dirTo(node.getAtX(), node.getAtY(), toX, toY));
				dirSet.addAll(Arrays.asList(OrthoDir.values()));
				for (OrthoDir dir : dirSet) {
					DirVector vec = dir.getVector();
					int nextX = (int) (vec.x + node.getAtX());
					int nextY = (int) (vec.y + node.getAtY());
					if (nextX >= 0 && nextX < map.getWidth() &&
						nextY >= 0 && nextY < map.getHeight() &&
						!visited.contains(new Vector2(nextX, nextY)) &&
						map.isChipPassable(nextX, nextY) &&
						map.isChipPassable(nextX+size, nextY-size)) {
						
						queue.add(new Path<OrthoDir>(node, dir));
					}
				}
			}
		}
		return null;
	}
	
	public List<OrthoDir> getPixelPath(MapEvent event) {
		Queue<Path<OrthoDir>> queue = new PriorityQueue<Path<OrthoDir>>();
		queue.add(new Path<OrthoDir>(toX, toY, fromX, fromY));
		// I can't believe I'm making a 2D array like this
		List<Vector2> visited = new ArrayList<Vector2>();
		
		while (queue.size() > 0) {
			Path<OrthoDir> node = queue.poll();
			if (visited.contains(new Vector2(node.getAtX(), node.getAtY()))) {
				// we've already been here
				continue;
			} else {
				visited.add(new Vector2(node.getAtX(), node.getAtY()));
				if (node.getAtX() == toX && node.getAtY() == toY) {
					//MGlobal.reporter.inform("Path found, expanded " + nodes);
					return node.getSteps();
				}
				for (OrthoDir dir : OrthoDir.values()) {
					DirVector vec = dir.getVector();
					int nextX = (int) (vec.x + node.getAtX());
					int nextY = (int) (vec.y + node.getAtY());
					if (nextX >= 0 && nextX < map.getWidth() &&
						nextY >= 0 && nextY < map.getHeight() &&
						!visited.contains(new Vector2(nextX, nextY)) &&
						map.willEventFit(event, nextX, nextY)) {
						
						queue.add(new Path<OrthoDir>(node, dir));
					}
				}
			}
		}
		return null;
	}
	
	protected OrthoDir dirTo(int fromX, int fromY, int toX, int toY) {
		int dx = toX - fromX;
		int dy = toY - fromY;
		if (Math.abs(dx) > Math.abs(dy)) {
			return Math.signum(dx) > 0 ? OrthoDir.EAST : OrthoDir.WEST;
		} else {
			return Math.signum(dy) > 0 ? OrthoDir.NORTH : OrthoDir.SOUTH;
		}
	}

}
