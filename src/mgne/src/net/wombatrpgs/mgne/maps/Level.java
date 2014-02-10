/**
 *  Level.java
 *  Created on Jan 3, 2014 8:02:39 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Turnable;
import net.wombatrpgs.mgne.graphics.effects.Effect;
import net.wombatrpgs.mgne.graphics.effects.EffectFactory;
import net.wombatrpgs.mgne.io.audio.MusicObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.maps.layers.EventLayer;
import net.wombatrpgs.mgne.maps.layers.GeneratedGridLayer;
import net.wombatrpgs.mgne.maps.layers.GridLayer;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.ScreenObject;
import net.wombatrpgs.mgneschema.audio.MusicMDO;
import net.wombatrpgs.mgneschema.maps.data.MapMDO;

/**
 * Superclass of both generated and loaded levels. It should probably have most
 * of the getters/setters used by pretty much everything. The old doc for
 * generated vs loaded levels is in those subclasses. In saga this thing should
 * be responsible for having tile/event layers, while the events themselves
 * should be on those layers. Mostly the functions here should be searches for
 * events across layers, passability checks, and turn management. Note that
 * turn management might be a bit weird as this whole thing is based on a
 * variable number of steps per turn, even if that's always 1 in an RPG, or
 * an infinitesmal amount in a rainfall-like ARPG.
 */
public abstract class Level extends ScreenObject implements Turnable {
	
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	
	protected MapMDO mdo;
	protected Screen screen;
	
	protected EventLayer eventLayer;
	protected List<GridLayer> gridLayers;
	
	protected List<MapThing> objects;
	protected List<MapEvent> removalEvents;
	protected List<MapThing> removalObjects;
	
	protected MusicObject bgm;
	protected Effect effect;
	protected boolean reseting;
	protected boolean updating;
	protected boolean moving;
	protected float moveTime;
	
	protected int mapWidth, mapHeight;
	
	/**
	 * Sets up a level, both generated and loaded stuff here.
	 * @param	mdo				The data to generate/load from
	 * @param	screen			The screen to associate with
	 */
	public Level(MapMDO mdo, Screen screen) {
		super(0);
		this.mdo = mdo;
		this.screen = screen;
		
		// list init
		gridLayers = new ArrayList<GridLayer>();
		objects = new ArrayList<MapThing>();
		removalObjects = new ArrayList<MapThing>();
		removalEvents = new ArrayList<MapEvent>();
		
		// etc
		if (MapThing.mdoHasProperty(mdo.effect)) {
			effect = EffectFactory.create(this, mdo.effect);
			assets.add(effect);
		}
		if (MapThing.mdoHasProperty(mdo.bgm)) {
			bgm = new MusicObject(MGlobal.data.getEntryFor(mdo.bgm, MusicMDO.class));
			assets.add(bgm);
		}
		reseting = false;
		moving = false;
	}
	
	/** @return The batch used to render sprites on this map */
	public SpriteBatch getBatch() { return MGlobal.levelManager.getScreen().getViewBatch(); }
	
	/** @return The width of this map, in pixels */
	public int getWidthPixels() { return getWidth() * getTileWidth(); }
	
	/** @return The height of this map, in pixels */
	public int getHeightPixels() { return getHeight() * getTileHeight(); }
	
	/** @return The width of this map, in tiles */
	public int getWidth() { return mapWidth; }
	
	/** @return The height of this map, in tiles */
	public int getHeight() { return mapHeight; }
	
	/** @return The width of each tile on this map, in pixels */
	public int getTileWidth() { return TILE_WIDTH; }
	
	/** @return The height of each tile on this map, in pixels */
	public int getTileHeight() { return TILE_HEIGHT; }
	
	/** @return The default bgm for this level */
	public MusicObject getBGM() { return this.bgm; }
	
	/** @param The new BGM object on this level */
	public void setBGM(MusicObject bgm) { this.bgm = bgm; }
	
	/** @return True if the map is between visible states, false otherwise */
	public boolean isMoving() { return this.moving; }
	
	/** @return The time remaining in the current move update, in s */
	public float getMoveTimeLeft() { return moveTime; }
	
	/** @return The time since the move started, in s */
	public float getMoveTimeElapsed() { return MGlobal.constants.getDelay() - moveTime; }
	
	/** @return The key to this map's mdo */
	public String getKey() { return mdo.key; }
	
	/** @return The screen this map is placed on */
	public Screen getScreen() { return MGlobal.levelManager.getScreen(); }
	
	/** @see net.wombatrpgs.mgne.screen.ScreenObject#ignoresTint() */
	@Override public boolean ignoresTint() { return false; }

	/** @see java.lang.Object#toString() */
	@Override public String toString() { return mdo.key; }
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.Renderable#render(
	 * com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		camera.update();
		for (GridLayer layer : gridLayers) {
			if (layer.getZ() < 1.f) {
				layer.render(camera);
			}
		}
		eventLayer.render(camera);
		for (GridLayer layer : gridLayers) {
			if (layer.getZ() >= 1.f) {
				layer.render(camera);
			}
		}
		if (effect != null) {
			effect.render(camera);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		updating = true;
		for (MapThing toRemove : removalObjects) {
			toRemove.onRemovedFromMap(this);
			internalRemoveObject(toRemove);
		}
		for (MapEvent toRemove : removalEvents) {
			internalRemoveEvent(toRemove);
		}
		removalObjects.clear();
		removalEvents.clear();
		for (int i = 0; i < objects.size(); i++) {
			MapThing object = objects.get(i);
			object.update(elapsed);
			if (reseting) break;
		}
		if (effect != null) {
			effect.update(elapsed);
		}
		reseting = false;
		updating = false;
		if (moving) {
			moveTime -= elapsed;
			if (moveTime <= 0) {
				stopMoving();
			}
		}
		
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		startMoving();
	}

	/**
	 * Adds a grid layer to the level. This really shouldn't be called by
	 * anyone but the map generator.
	 * @param	layer			The layer to add
	 */
	public void addGridLayer(GeneratedGridLayer layer) {
		gridLayers.add(layer);
	}
	
	/**
	 * Sets all events moving on their merry way towards their destinations!
	 * Meant to be called by the hero when they make a move. This integrates
	 * all other events and then starts them all moving.
	 */
	public void startMoving() {
		// integration step
		eventLayer.integrate();
		
		// move step
		moving = true;
		moveTime = MGlobal.constants.getDelay();
		for (MapEvent event : eventLayer.getEvents()) {
			event.startMoving();
		}
	}
	
	/**
	 * Stops all events from moving. This should probably be private but
	 * basically all it does is tell the event layer to stop. Gets called from
	 * update on timeout.
	 */
	public void stopMoving() {
		moving = false;
		for (MapEvent event : eventLayer.getEvents()) {
			if (event.getParent() == this) {
				event.stopMoving();
			}
		}
	}
	
	/**
	 * Checks if a certain tile is passable by chip. This does not take into
	 * account event passability.
	 * @param	actor			The character that will be trying to pass
	 * @param 	tileX			The checked x-coord (in tiles)
	 * @param 	tileY			The checked y-coord (in tiles)
	 * @return 					True if layer is passable, false otherwise
	 */
	public boolean isTilePassable(MapEvent actor, int tileX, int tileY) {
		for (GridLayer layer : gridLayers) {
			if (!layer.isPassable(actor, tileX, tileY)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Removes an event from this map. The object is assumed not to be the hero.
	 * Control remains on this map.
	 * @param 	toRemove		The map event to remove
	 */
	public void removeEvent(MapEvent toRemove) {
		removalEvents.add(toRemove);
	}
	
	/**
	 * Internall removes an object from all lists and registries. This should
	 * not be used for events, at least not as a primary call.
	 * @param 	toRemove		The event to remove
	 */
	public void removeObject(MapThing toRemove) {
		removalObjects.add(toRemove);
	}
	
	/**
	 * Welcomes a new event to this map. Does not transfer level control. Event
	 * is assumed to not be the hero. Z is set to 0.
	 * @param 	newEvent		The event to teleport in
	 * @param 	tileX			The initial x-coord (in tiles) of this object
	 * @param 	tileY			The initial y-coord (in tiles) of this object
	 */
	public void addEvent(MapEvent newEvent, int tileX, int tileY) {
		addEventAbsolute(newEvent, tileX * getTileWidth(), tileY * getTileHeight());
		newEvent.setTileX(tileX);
		newEvent.setTileY(tileY);
	}
	
	/**
	 * Another clone for adding events. This one used float coords for pixels
	 * instead of the usual tile coordinates.
	 * @param 	newEvent		The event to add
	 * @param 	x				The x-coord of the object (in px)
	 * @param 	y				The y-coord of the object (in px)
	 */
	public void addEventAbsolute(MapEvent newEvent, float x, float y) {
		newEvent.setX(x);
		newEvent.setY(y);
		addEvent(newEvent);
	}
	
	/**
	 * Adds a new event to this map. Called internally for maps in the actual
	 * map resources and externally by events that should've been there but
	 * aren't for convenience reasons.
	 * @param 	newEvent		The new event to add
	 */
	public void addEvent(MapEvent newEvent) {
		eventLayer.add(newEvent);
		addObject(newEvent);
	}
	
	/**
	 * Finds and returns the event named apporpriately. Behaves weirdly if
	 * multiple events have the same name.
	 * @param 	name			The name of the event we're looking for
	 * @return					An event named that way, or null if none
	 */
	public MapEvent getEventByName(String name) {
		return eventLayer.getEventByName(name);
	}
	
	/**
	 * Finds and returns all events with the given group name. Numerous for
	 * commanding a bunch of events at once. Events can belong to multiple
	 * groups by putting the ';' between their group name.
	 * @param 	groupName		The group name to search for
	 * @return					All events belonging to that group
	 */
	public List<MapEvent> getEventsByGroup(String groupName) {
		return eventLayer.getEventsByGroup(groupName);
	}
	
	/**
	 * Finds and returns all events at a given location.
	 * @param	tileX			The x-coord of the event to find, in tiles
	 * @param	tileY			The y-coord of the event to find, in tiles
	 * @return					All events there, or empty if none
	 */
	public List<MapEvent> getEventsAt(int tileX, int tileY) {
		return eventLayer.getEventsAt(tileX, tileY);
	}
	
	/**
	 * Adds a new object to this map. Called externally for anything wanting to
	 * add non-events to this map.
	 * @param 	object			The new object to add
	 */
	public void addObject(MapThing object) {
		if (objects.contains(object)) {
			if (removalEvents.contains(object)) {
				internalRemoveEvent((MapEvent) object);
				MGlobal.reporter.inform("Overlapped remove/add event: " + object);
			} else if (removalObjects.contains(object)) {
				internalRemoveObject(object);
				MGlobal.reporter.inform("Overlapped remove/add object: " + object);
			} else {
				MGlobal.reporter.warn("Added the same object twice: " + object);
				return;
			}
		}
		objects.add(object);
		object.onAddedToMap(this);
	}
	
	/**
	 * Resets the level to how it was during its intial load. This should keep
	 * important things like puzzle solved status but remove things like enemy
	 * deaths and reset event positions.
	 */
	public void reset() {
		reseting = true;
		for (MapThing object : objects) {
			object.reset();
		}
	}
	
	/**
	 * Called when hero goes somewhere else or map otherwise ceases to be.
	 */
	public void onFocusLost() {
		for (MapThing object : objects) {
			object.onMapFocusLost(this);
		}
	}
	
	/**
	 * Determiens if an object will exist on this level in the upcoming update.
	 * This doesn't check the active objects, but also the objects in the queue
	 * for adding and excludes the removal queue. (actually there is no add
	 * queue right now so uh)
	 * @param 	object			The object to check if exists
	 * @return					True if that object will be on the map
	 */
	public boolean contains(MapThing object) {
		for (MapEvent victim : removalEvents) {
			if (object == victim) return false;
		}
		for (MapThing victim : removalObjects) {
			if (object == victim) return false;
		}
		for (MapThing other : objects) {
			if (object == other) return true;
		}
		return false;
	}
	
	/**
	 * Internally removes an event from all lists and registries.
	 * @param 	toRemove		The event to remove
	 */
	protected void internalRemoveEvent(MapEvent toRemove) {
		internalRemoveObject(toRemove);
		eventLayer.remove(toRemove);
	}
	
	/**
	 * Called when this object is removed from the map.
	 * @param 	toRemove		The object to remove
	 */
	protected void internalRemoveObject(MapThing toRemove) {
		toRemove.onRemovedFromMap(this);
		objects.remove(toRemove);
	}

}
