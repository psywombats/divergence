/**
 *  Level.java
 *  Created on Nov 12, 2012 6:08:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.graphics.effects.Effect;
import net.wombatrpgs.mrogue.graphics.effects.EffectFactory;
import net.wombatrpgs.mrogue.io.audio.MusicObject;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.maps.gen.MapGenerator;
import net.wombatrpgs.mrogue.maps.gen.MapGeneratorFactory;
import net.wombatrpgs.mrogue.maps.layers.EventLayer;
import net.wombatrpgs.mrogue.maps.layers.GridLayer;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.CharacterFactory;
import net.wombatrpgs.mrogue.rpg.Enemy;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogue.screen.ScreenObject;
import net.wombatrpgs.mrogueschema.audio.MusicMDO;
import net.wombatrpgs.mrogueschema.characters.data.CharacterMDO;
import net.wombatrpgs.mrogueschema.items.ItemGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.MapMDO;
import net.wombatrpgs.mrogueschema.maps.MonsterGeneratorMDO;

/**
 * A Level is comprised of a .tmx tiled map background and a bunch of events
 * that populate it. I can hear you already, "IT'S CALLED A MAP." No need to 
 * conflict with the data structure. Anyway this thing is a wrapper around Tiled
 * with a few RPG-specific functions built in, like rendering its layers in
 * order so that the player's sprite can appear say above the ground but below a
 * cloud or other upper chip object.
 * 
 * MR note: This is no longer linked to a .tmx file!! This is a strange thing.
 * Instead, it uses a generator to generate itself. This means there will be a
 * whole mess of variables in here related to tile properties and bullshit. This
 * is mitigated by having the old layer handle most of themselves. In 99% of
 * cases though, all layers but 1 gridlayer (the map layout) and 1 event layer
 * (everything on it) will be empty.
 * Also note that there is only one z layer for objects.
 */
public class Level extends ScreenObject implements Turnable {
	
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	
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
	
	protected Map<String, Loc> teleLocations; // string ID to incoming location
	
	// MR mappy stuff
	protected int mapWidth, mapHeight;
	protected MapGenerator mapGen;
	protected ItemGenerator itemGen, lootGen;
	protected MonsterGenerator monGen;
	protected SceneParser scene;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo				The data to make level from
	 * @param	screen			The screen we render to
	 */
	public Level(MapMDO mdo, Screen screen) {
		super(0);
		this.screen = screen;
		this.mdo = mdo;
		
		// list init
		gridLayers = new ArrayList<GridLayer>();
		objects = new ArrayList<MapThing>();
		removalObjects = new ArrayList<MapThing>();
		removalEvents = new ArrayList<MapEvent>();
		teleLocations = new HashMap<String, Loc>();
		
		// etc
		if (MapThing.mdoHasProperty(mdo.effect)) {
			effect = EffectFactory.create(this, mdo.effect);
			assets.add(effect);
		}
		if (MapThing.mdoHasProperty(mdo.enemies)) {
			monGen = new MonsterGenerator(this,
					MGlobal.data.getEntryFor(mdo.enemies, MonsterGeneratorMDO.class));
			assets.add(monGen);
		}
		if (MapThing.mdoHasProperty(mdo.items)) {
			itemGen = new ItemGenerator(this,
					MGlobal.data.getEntryFor(mdo.items, ItemGeneratorMDO.class));
			assets.add(itemGen);
		}
		if (MapThing.mdoHasProperty(mdo.goodItems)) {
			lootGen = new ItemGenerator(this,
					MGlobal.data.getEntryFor(mdo.goodItems, ItemGeneratorMDO.class));
			assets.add(lootGen);
		} else if (itemGen != null) {
			lootGen = itemGen;
		}
		if (MapThing.mdoHasProperty(mdo.scene)) {
			scene = MGlobal.levelManager.getCutscene(mdo.scene);
			assets.add(scene);
		}
		if (MapThing.mdoHasProperty(mdo.bgm)) {
			bgm = new MusicObject(MGlobal.data.getEntryFor(mdo.bgm, MusicMDO.class));
			assets.add(bgm);
		}
		reseting = false;
		moving = false;
		
		// map gen!
		eventLayer = new EventLayer(this);
		assets.add(eventLayer);
		this.mapGen = MapGeneratorFactory.createGenerator(
				MGlobal.data.getEntryFor(mdo.generator, MapGeneratorMDO.class),
				this);
		assets.add(mapGen);
		this.mapWidth = mdo.mapWidth;
		this.mapHeight = mdo.mapHeight;
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
	
	/** @return The thing in charge of making monsters for us */
	public MonsterGenerator getMonsterGenerator() { return monGen; }
	
	/** @reutrn The thing in charge of making items for us */
	public ItemGenerator getItemGenerator() { return itemGen; }
	
	/** @reutrn The thing in charge of making good items for us */
	public ItemGenerator getLootGenerator() { return lootGen; }
	
	/** @return The time since the move started, in s */
	public float getMoveTimeElapsed() { return MGlobal.constants.getDelay() - moveTime; }
	
	/** @return The number of characters on the map */
	public int getPopulation() { return eventLayer.getCharacters().size(); }
	
	/** @return All the characters currently on this map */
	public List<CharacterEvent> getCharacters() { return eventLayer.getCharacters(); }
	
	/** @return The key to this map's mdo */
	public String getKey() { return mdo.key; }
	
	/** @return The map keys of all neighbors that are reached by ascending */
	public String[] getUpKeys() { return mdo.pathsUp; }
	
	/** @return The map keys of all neighbors that are reached by descending */
	public String[] getDownKeys() { return mdo.pathsDown; }
	
	/** @return The screen this map is placed on */
	public Screen getScreen() { return MGlobal.levelManager.getScreen(); }
	
	/** @return The danger level of this map */
	public int getDanger() { return mdo.danger; }
	
	/** @see net.wombatrpgs.mrogue.screen.ScreenObject#ignoresTint() */
	@Override public boolean ignoresTint() { return false; }

	/** @see java.lang.Object#toString() */
	@Override public String toString() { return mdo.key; }
	
	/** @return The UI floor of this level */
	public String getFloor() { return mdo.floor; }

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render(
	 * com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
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
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, 0);
		if (pass == 0) {
			mapGen.generateMe();
			if (mdo.characters != null) {
				for (String key : mdo.characters) {
					CharacterMDO charaMDO = MGlobal.data.getEntryFor(key, CharacterMDO.class);
					CharacterEvent chara = CharacterFactory.create(charaMDO, this);
					chara.spawnUnseen();
					assets.add(chara);
					chara.queueRequiredAssets(manager);
				}
			}
		}
		if (monGen != null) {
			monGen.spawnToDensity();
		}
		if (itemGen != null) {
			itemGen.spawnOnCreate();
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		updating = true;
		if (MGlobal.stasis) return;
		if (scene != null && !scene.hasExecuted()) {
			scene.run();
		}
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
				moving = false;
				for (MapEvent event : eventLayer.getEvents()) {
					if (event.getParent() == this) {
						event.stopMoving();
					}
				}
			}
		}
		
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		if (monGen != null) {
			monGen.onTurn();
		}
		startMoving();
	}

	/**
	 * Adds a grid layer to the level. This really shouldn't be called by
	 * anyone but the map generator.
	 * @param	layer			The layer to add
	 */
	public void addGridLayer(GridLayer layer) {
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
			event.onMoveStart();
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
		if (newEvent == MGlobal.hero && scene != null) {
			scene.run();
		}
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
	 * Looks up where to teleport in were a hero to try and warp from the
	 * designated map.
	 * @param	mapID			The MDO key of the map to look up
	 * @return					The location of incoming characters from there
	 */
	public Loc getTeleInLoc(String mapID) {
		Loc loc = teleLocations.get(mapID);
		if (loc == null) {
			MGlobal.reporter.err("No map found from " + this + " to " + mapID);
		}
		return loc;
	}
	
	/**
	 * Designates a teleport location for a specific map. Complains if we
	 * already have a path to that map. Note that this prevents a map from
	 * having multiple connections to the same map, but not >2 connections in
	 * general.
	 * @param	mapID			The ID of the map teleporting from
	 * @param	teleLoc			The location on this map where we arrive
	 */
	public void setTeleInLoc(String mapID, Loc arriveLoc) {
		teleLocations.put(mapID, arriveLoc);
	}
	
	/**
	 * Determines whether a certain location is see-through.
	 * @param	tileX			The x-coord of the tile to check, in tiles
	 * @param	tileY			The y-coord of the tile to check, in tiles
	 * @return					True if tile is transparent, false otherwise
	 */
	public boolean isTransparentAt(int tileX, int tileY) {
		// TODO: precompute this
		if (tileX < 0 || tileY < 0 || tileX >= mapWidth || tileY >= mapHeight) {
			return false;
		}
		for (GridLayer layer : gridLayers) {
			if (!layer.isTransparentAt(tileX, tileY)) {
				return false;
			}
		}
		for (MapEvent event : eventLayer.getEventsAt(tileX, tileY)) {
			if (!event.isTransparent()) return false;
		}
		return true;
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
	 * Generates an enemy for this map.
	 * @return					The enemy generated, or null if no generator
	 */
	public Enemy generateEnemy() {
		if (monGen == null) return null;
		return monGen.createEnemy();
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
