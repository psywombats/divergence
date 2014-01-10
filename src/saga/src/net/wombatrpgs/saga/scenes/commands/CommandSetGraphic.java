/**
 *  CommandSetGraphic.java
 *  Created on Mar 7, 2013 2:16:54 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes.commands;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.graphics.FacesAnimation;
import net.wombatrpgs.saga.graphics.FacesAnimationFactory;
import net.wombatrpgs.saga.maps.events.MapEvent;
import net.wombatrpgs.saga.rpg.CharacterEvent;
import net.wombatrpgs.saga.scenes.SceneCommand;
import net.wombatrpgs.saga.scenes.SceneParser;
import net.wombatrpgs.sagaschema.graphics.DirMDO;

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
			SGlobal.reporter.warn("Graphic group assigments aren't really implemented yet");
		}
		if (eventName.equals(ARG_HERO)) {
			events.add(SGlobal.getHero());
		} else {
			events.add(parent.getLevel().getEventByName(eventName));
		}
		if (events.get(0) == null) {
			SGlobal.reporter.warn("Couldn't find ev to set anim: " + eventName);
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
				SGlobal.reporter.warn("Unknown arg to setgraphic: " + arg);
			}
		}
		DirMDO dirMDO = SGlobal.data.getEntryFor(mdoKey, DirMDO.class);
		graphic = FacesAnimationFactory.create(dirMDO, events.get(0));
		assets.add(graphic);
	}

	/**
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#run()
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
				SGlobal.reporter.warn("Tried to set anim of non-character: " + mapEvent);
			}
		}
		finished = true;
		return false;
	}

}
