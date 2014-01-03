/**
 *  CommandMove.java
 *  Created on Feb 5, 2013 12:39:42 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.PauseLevel;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogueschema.maps.data.DirVector;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * A command to move a character a certain distance a certain direction,
 * or potentially lots of directions.
 */
public class CommandMove extends SceneCommand {
	
	protected static final String DIR_UP = "up";
	protected static final String DIR_DOWN = "down";
	protected static final String DIR_LEFT = "left";
	protected static final String DIR_RIGHT = "right";
	protected static final String FACEDIR_UP = "face-up";
	protected static final String FACEDIR_DOWN = "face-down";
	protected static final String FACEDIR_LEFT = "face-left";
	protected static final String FACEDIR_RIGHT = "face-right";
	
	protected static final String ARG_GROUP = "group";
	
	protected String eventName, groupName;
	protected Stack<MoveStep> steps;
	protected List<MapEvent> events;
	protected String movesLine;

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
		this.eventName = line.substring(0, line.indexOf(' '));
		if (eventName.equals(ARG_GROUP)) {
			line = line.substring(line.indexOf(' ') + 1);
			groupName = line.substring(0, line.indexOf(' '));
		}
		line = line.substring(line.indexOf(' ') + 1);
		events = new ArrayList<MapEvent>();
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
		events = new ArrayList<MapEvent>();
		events.add(event);
		finished = false;
		steps = new Stack<MoveStep>();
		parseSteps(line);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		if (events.size() == 0) {
			if (eventName.equals(ARG_GROUP)) {
				events.addAll(parent.getLevel().getEventsByGroup(groupName));
			} else {
				events.add(parent.getLevel().getEventByName(eventName));
				if (events.get(0) == null) {
					MGlobal.reporter.warn("Couldn't move event by name: " + eventName);
				}
			}
		}
		boolean allDone = true;
		for (MapEvent event : events) {
			if (!event.isTracking() && steps.size() <= 0 && !finished) {
				event.halt();
			} else {
				allDone = false;
			}
			if (event.isTracking()) {
				event.setPauseLevel(PauseLevel.PAUSE_RESISTANT);
			} else {
				event.setPauseLevel(PauseLevel.SURRENDERS_EASILY);
			}
		}
		boolean needsTracking = false;
		for (MapEvent event : events) {
			if (!event.isTracking() && steps.size() > 0) {
				needsTracking = true;
			}
		}
		if (needsTracking) {
			MoveStep step = steps.pop();
			for (MapEvent mapEvent : events) {
				mapEvent.targetLocation(
						mapEvent.getX() + step.deltaX, 
						mapEvent.getY() + step.deltaY);
				if (step.dir != null) {
					if (CharacterEvent.class.isAssignableFrom(mapEvent.getClass())) { 
						CharacterEvent event = (CharacterEvent) mapEvent;
						event.setFacing(step.dir.toOrtho(event.getFacing()));
					} else {
						MGlobal.reporter.warn("Tried to set dir of non-character: " + mapEvent);
					}
				}
				if (!parent.getControlledEvents().contains(mapEvent)) {
					parent.getControlledEvents().add(mapEvent);
				}
			}
		}
		finished = allDone;
		return true;
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		steps = new Stack<MoveStep>();
		parseSteps(movesLine);
	}

	/**
	 * Parses all the data contained in the latter half of the string, the part
	 * reserved for the move commands.
	 * @param 	line			What remains of the line code
	 */
	protected void parseSteps(String line) {
		movesLine = line.substring(0);
		while(!line.equals("")) {
			String dirString;
			if (line.indexOf(' ') != -1) {
				dirString = line.substring(0, line.indexOf(' '));
				line = line.substring(line.indexOf(' ') + 1);
			} else {
				dirString = line.substring(0, line.indexOf(']'));
				line = "";
			}
			
			EightDir dir = null;
			if (dirString.equals(DIR_UP)) dir = EightDir.NORTH;
			if (dirString.equals(DIR_DOWN)) dir = EightDir.SOUTH;
			if (dirString.equals(DIR_LEFT)) dir = EightDir.WEST;
			if (dirString.equals(DIR_RIGHT)) dir = EightDir.EAST;
			if (dir == null) {
				if (dirString.equals(FACEDIR_UP)) dir = EightDir.NORTH;
				if (dirString.equals(FACEDIR_DOWN)) dir = EightDir.SOUTH;
				if (dirString.equals(FACEDIR_LEFT)) dir = EightDir.WEST;
				if (dirString.equals(FACEDIR_RIGHT)) dir = EightDir.EAST;
				if (dir == null) MGlobal.reporter.warn("Non-dir string in move " +
						"command" + dirString);
				steps.add(0, new MoveStep(dir));
			} else {
				DirVector vec = dir.getVector();
				String countString;
				if (line.indexOf(' ') == -1) {
					countString = line.substring(0, line.indexOf(']'));
					line = "";
				} else {
					countString = line.substring(0, line.indexOf(' '));
					line = line.substring(line.indexOf(' ') + 1);
				}
				int paces = Integer.valueOf(countString);
				int deltaX = (int) vec.x * paces * parent.getLevel().getTileWidth();
				int deltaY = (int) vec.y * paces * parent.getLevel().getTileHeight();
				steps.add(0, new MoveStep(deltaX, deltaY));
			}

		}
	}

	public class MoveStep {
		public int deltaX, deltaY;
		public EightDir dir;
		public MoveStep(int deltaX, int deltaY) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}
		public MoveStep(EightDir dir) {
			this.dir = dir;
			deltaX = 0;
			deltaY = 0;
		}
	}
}
