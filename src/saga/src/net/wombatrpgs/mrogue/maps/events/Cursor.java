/**
 *  Cursor.java
 *  Created on Oct 25, 2013 9:28:46 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.FinishListener;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.AnimationStrip;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.io.CommandMap;
import net.wombatrpgs.mrogue.io.command.CMapDirections;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * Thing that moves around on the map for Look mode.
 */
public class Cursor extends MapEvent implements CommandListener {
	
	protected AnimationStrip appearance;
	protected CommandMap commands;
	protected CharacterEvent lastTarget;
	protected List<FinishListener> listeners;
	protected float range;
	protected boolean targetingMode;
	
	/**
	 * Creates a new cursor using the data found in the global UI settings.
	 */
	public Cursor() {
		appearance = MGlobal.ui.getCursor();
		appearance.setParent(this);
		commands = new CMapDirections();
		listeners = new ArrayList<FinishListener>();
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_NORTH:		return move(EightDir.NORTH);
		case MOVE_NORTHEAST:	return move(EightDir.NORTHEAST);
		case MOVE_EAST:			return move(EightDir.EAST);
		case MOVE_SOUTHEAST:	return move(EightDir.SOUTHEAST);
		case MOVE_SOUTH:		return move(EightDir.SOUTH);
		case MOVE_SOUTHWEST:	return move(EightDir.SOUTHWEST);
		case MOVE_WEST:			return move(EightDir.WEST);
		case MOVE_NORTHWEST:	return move(EightDir.NORTHWEST);
		case INTENT_CANCEL:		deactivate(false);	return true;
		case INTENT_CONFIRM:	deactivate(true);	return true;
		default: return false;
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		this.renderLocal(camera, appearance.getRegion(), -8, -16, 0);
	}
	
	/** @return The thing the cursor was last over */
	public CharacterEvent getLastTarget() { return lastTarget; }
	
	/** @param listener The listener to await when this cursor is canceled */
	public void registerFinishListener(FinishListener listener) { listeners.add(listener); }
	
	/** @param listener The listener to stop waiting for when this cursor is canceled */
	public void unregisterFinishListener(FinishListener listener) { listeners.remove(listener); }
	
	/** @return True if the cursor is currently on the screen */
	public boolean isActive() { return MGlobal.levelManager.getActive().contains(this); }
	
	/** @param The new max range radius of the cursor */
	public void setRange(float range) { this.range = range; }
	
	/** @param The last last target */
	public void setLastTarget(CharacterEvent target) { this.lastTarget = target; }
	
	/**
	 * Adds selve to map and initiates look mode.
	 * @param	targetingMode	True to enable target as opposed to look mode
	 */
	public void activate(boolean targetingMode) {
		this.targetingMode = targetingMode;
		lastTarget = null;
		MGlobal.levelManager.getActive().addEvent(
				this,
				MGlobal.hero.getTileX(),
				MGlobal.hero.getTileY());
		parent.getScreen().pushCommandContext(commands);
		parent.getScreen().registerCommandListener(this);
	}
	
	/**
	 * On look mode cancel.
	 * @param	confirmed		True if this was cancelled via positive intent
	 */
	public void deactivate(boolean confirmed) {
		parent.removeEvent(this);
		parent.getScreen().popCommandContext();
		parent.getScreen().unregisterCommandListener(this);
		if (!confirmed) lastTarget = null;
		for (FinishListener listener : listeners) {
			listener.onFinish();
		}
		listeners.clear();
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getZ()
	 */
	@Override
	protected float getZ() {
		return super.getZ() + .5f;
	}

	/**
	 * Takes a step in that direction.
	 * @param	dir				The direction to step in
	 * @return					True
	 */
	protected boolean move(EightDir dir) {
		int targetX = (int) (getTileX() + dir.getVector().x);
		int targetY = (int) (getTileY() + dir.getVector().y);
		if (targetX < 0 || targetX >= parent.getWidth()) return true;
		if (targetY < 0 || targetY >= parent.getHeight()) return true;
		if (!MGlobal.hero.seen(targetX, targetY)) return true;
		
		if (targetingMode) {
			if (MGlobal.hero.euclideanTileDistanceTo(targetX, targetY) > range) {
				MGlobal.ui.getNarrator().msg("Out of range.");
				return true;
			}
		}
		setTileX(targetX);
		setTileY(targetY);
		setX(getTileX() * parent.getTileWidth());
		setY(getTileY() * parent.getTileHeight());
		
		if (!MGlobal.hero.inLoS(targetX, targetY)) {
			MGlobal.ui.getNarrator().msg("Can't see that.");
			return true;
		}
		
		if (targetingMode) {
			lastTarget = null;
			for (CharacterEvent chara : MGlobal.hero.getParent().getCharacters()) {
				if (chara.getTileX() == tileX && chara.getTileY() == tileY) {
					String text = "Target: " + chara.getUnit().getName();
					lastTarget = chara;
					MGlobal.ui.getNarrator().msg(text);
				}
			}
		} else {
			for (MapEvent event : parent.getEventsAt(targetX, targetY)) {
				String text = null;
				text = event.mouseoverMessage();
				if (text != null && text.length() > 0) {
					MGlobal.ui.getNarrator().msg(text);
				}
			}
		}
		
		return true;
	}

}
