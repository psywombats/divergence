/**
 *  CommandSetGraphic.java
 *  Created on Mar 7, 2013 2:16:54 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;

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
			RGlobal.reporter.warn("Graphic group assigments aren't really implemented yet");
		}
		if (eventName.equals(ARG_HERO)) {
			events.add(RGlobal.hero);
		} else {
			events.add(parent.getLevel().getEventByName(eventName));
		}
		if (events.get(0) == null) {
			RGlobal.reporter.warn("Couldn't find ev to set anim: " + eventName);
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
				RGlobal.reporter.warn("Unknown arg to setgraphic: " + arg);
			}
		}
		DirMDO dirMDO = RGlobal.data.getEntryFor(mdoKey, DirMDO.class);
		graphic = FacesAnimationFactory.create(dirMDO, events.get(0));
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
		for (MapEvent mapEvent : events) {
			if (CharacterEvent.class.isAssignableFrom(mapEvent.getClass())) { 
				CharacterEvent event = (CharacterEvent) mapEvent;
				event.setWalkAnim(graphic);
				event.setPacing(autoplay);
				event.halt();
			} else {
				RGlobal.reporter.warn("Tried to set anim of non-character: " + mapEvent);
			}
		}
		finished = true;
		return false;
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		graphic.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		graphic.postProcessing(manager, pass);
	}

}
