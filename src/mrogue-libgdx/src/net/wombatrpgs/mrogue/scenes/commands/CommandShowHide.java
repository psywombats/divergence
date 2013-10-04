/**
 *  CommandShow.java
 *  Created on Mar 6, 2013 6:36:46 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * Reveals a bunch of characters on the map that are hidden.
 */
public class CommandShowHide extends SceneCommand {
	
	protected static final String ARG_GROUP = "group";
	
	protected String eventName, groupName;
	protected boolean invert;

	/**
	 * Inherited constructor.
	 * @param 	parent			The parent parser
	 * @param 	line			The line of code
	 * @param	invert			True if this is a hide rather than a show
	 */
	public CommandShowHide(SceneParser parent, String line, boolean invert) {
		super(parent, line);
		this.invert = invert;
		line = line.substring(line.indexOf(' ')+1);
		if (line.indexOf(' ') != -1) {
			this.eventName = line.substring(0, line.indexOf(' '));
		} else {
			this.eventName = line.substring(0, line.indexOf(']'));
		}
		if (eventName.equals(ARG_GROUP)) {
			line = line.substring(line.indexOf(' ') + 1);
			groupName = line.substring(0, line.indexOf(']'));
		}
		line = line.substring(line.indexOf(' ') + 1);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		List<MapEvent> events = new ArrayList<MapEvent>();
		if (events.size() == 0) {
			if (eventName.equals(ARG_GROUP)) {
				events.addAll(parent.getLevel().getEventsByGroup(groupName));
			} else {
				events.add(parent.getLevel().getEventByName(eventName));
				if (events.get(0) == null) {
					MGlobal.reporter.warn("Show/hide a null event named: " + eventName);
				}
			}
		}
		for (MapEvent event : events) {
			if (event != null) event.setCommandHidden(invert);
		}
		finished = true;
		return false;
	}

}
