/**
 *  CommandMove.java
 *  Created on Feb 5, 2013 12:39:42 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import java.util.Stack;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.PauseLevel;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.maps.data.DirVector;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * A command to move a character a certain distance a certain direction,
 * or potentially lots of directions.
 */
public class CommandMove extends SceneCommand {
	
	protected static final String DIR_UP = "up";
	protected static final String DIR_DOWN = "down";
	protected static final String DIR_LEFT = "left";
	protected static final String DIR_RIGHT = "right";
	
	protected Stack<MoveStep> steps;
	protected MapEvent event;

	/**
	 * Creates a movement command and interprets its string to get it all ready
	 * for execution by the parser.
	 * @param 	parent			The parser that will be executing us
	 * @param 	line			The line of code that spawned us
	 */
	public CommandMove(SceneParser parent, String line) {
		super(parent, line);
		steps = new Stack<MoveStep>();
		line = line.substring(line.indexOf(' ')+1);
		String eventName = line.substring(0, line.indexOf(' '));
		event = parent.getLevel().getEventByName(eventName);
		line = line.substring(line.indexOf(' ') + 1);
		this.line = line;
		parseSteps(line);
	}
	
	/**
	 * If any of our buddy commands need to move something, come here.
	 * @param	parent			The parent scene parser
	 * @param 	line			The move string code
	 * @param 	event			The thing that'll be moved
	 */
	public CommandMove(SceneParser parent, String line, MapEvent event) {
		super(parent, line);
		this.event = event;
		finished = false;
		steps = new Stack<MoveStep>();
		parseSteps(line);
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!event.isTracking() && steps.size() <= 0 && !finished) {
			event.halt();
			finished = true;
		}
		if (finished) return true;
		if (event.isTracking()) {
			event.setPauseLevel(PauseLevel.PAUSE_RESISTANT);
		} else {
			event.setPauseLevel(PauseLevel.SURRENDERS_EASILY);
		}
		if (!event.isTracking() && steps.size() > 0) {
			MoveStep step = steps.pop();
			event.targetLocation(
					event.getX() + step.deltaX, 
					event.getY() + step.deltaY);
			if (!parent.getControlledEvents().contains(event)) {
				parent.getControlledEvents().add(event);
			}
		}
		return true;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		steps = new Stack<MoveStep>();
		parseSteps(line);
	}

	/**
	 * Parses all the data contained in the latter half of the string, the part
	 * reserved for the move commands.
	 * @param 	line			What remains of the line code
	 */
	protected void parseSteps(String line) {
		while(!line.equals("")) {
			String dirString = line.substring(0, line.indexOf(' '));
			line = line.substring(line.indexOf(' ') + 1);
			String countString;
			if (line.indexOf(' ') == -1) {
				countString = line.substring(0, line.indexOf(']'));
				line = "";
			} else {
				countString = line.substring(0, line.indexOf(' '));
				line = line.substring(line.indexOf(' ') + 1);
			}
			
			Direction dir = null;
			if (dirString.equals(DIR_UP)) dir = Direction.UP;
			if (dirString.equals(DIR_DOWN)) dir = Direction.DOWN;
			if (dirString.equals(DIR_LEFT)) dir = Direction.LEFT;
			if (dirString.equals(DIR_RIGHT)) dir = Direction.RIGHT;
			if (dir == null) RGlobal.reporter.warn("Non-dir string in move " +
					"command" + dirString);
			DirVector vec = dir.getVector();
			
			int paces = Integer.valueOf(countString);
			int deltaX = vec.x * paces * parent.getLevel().getTileWidth();
			int deltaY = vec.y * paces * parent.getLevel().getTileHeight();
			steps.add(0, new MoveStep(deltaX, deltaY));
		}
	}

	public class MoveStep {
		public int deltaX, deltaY;
		public MoveStep(int deltaX, int deltaY) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}
	}
}
