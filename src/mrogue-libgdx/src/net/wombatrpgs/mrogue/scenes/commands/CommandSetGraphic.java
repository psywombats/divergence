/**
 *  CommandSetGraphic.java
 *  Created on Mar 7, 2013 2:16:54 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.FacesAnimation;
import net.wombatrpgs.mrogue.graphics.FacesAnimationFactory;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogueschema.graphics.DirMDO;

/**
 * Sets an event to look like something.
 */
public class CommandSetGraphic extends SceneCommand {
	
	protected static final String ARG_GROUP = "group";
	protected static final String ARG_HERO = "hero";
	protected static final String ARG_AUTOPLAY = "autoplay";
	
	protected List<MapEvent> events;
	protected String eventName; //, groupName;
	protected FacesAnimation graphic;
	protected boolean autoplay;

	/**
	 * Inherited constructor.
	 * @param 	parent			Parent parser
	 * @param 	line			Line of code
	 */
	public CommandSetGraphic(SceneParser parent, String line) {
		super(parent, line);
		autoplay = false;
		events = new ArrayList<MapEvent>();
		line = line.substring(line.indexOf(' ')+1);
		this.eventName = line.substring(0, line.indexOf(' '));
		if (eventName.equals(ARG_GROUP)) {
//			line = line.substring(line.indexOf(' ') + 1);
//			groupName = line.substring(0, line.indexOf(' '));
			MGlobal.reporter.warn("Graphic group assigments aren't really implemented yet");
		}
		if (eventName.equals(ARG_HERO)) {
			events.add(MGlobal.hero);
		} else {
			events.add(parent.getLevel().getEventByName(eventName));
		}
		if (events.get(0) == null) {
			MGlobal.reporter.warn("Couldn't find ev to set anim: " + eventName);
		}
		line = line.substring(line.indexOf(' ') + 1);
		String mdoKey;
		if (line.indexOf(' ') == -1) {
			mdoKey = line.substring(0, line.indexOf(']'));
		} else {
			mdoKey = line.substring(0, line.indexOf(' '));
			line = line.substring(line.indexOf(' ') + 1);
			String arg = line.substring(0, line.indexOf(']'));
			if (arg.equals(ARG_AUTOPLAY)) {
				autoplay = true;
			} else {
				MGlobal.reporter.warn("Unknown arg to setgraphic: " + arg);
			}
		}
		DirMDO dirMDO = MGlobal.data.getEntryFor(mdoKey, DirMDO.class);
		graphic = FacesAnimationFactory.create(dirMDO, events.get(0));
		assets.add(graphic);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		for (MapEvent mapEvent : events) {
			if (CharacterEvent.class.isAssignableFrom(mapEvent.getClass())) { 
				CharacterEvent event = (CharacterEvent) mapEvent;
				event.setAppearance(graphic);
				event.setPacing(autoplay);
				event.halt();
			} else {
				MGlobal.reporter.warn("Tried to set anim of non-character: " + mapEvent);
			}
		}
		finished = true;
		return false;
	}

}
