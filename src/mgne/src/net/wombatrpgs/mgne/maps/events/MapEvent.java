/**
 *  MapEvent.java
 *  Created on Dec 24, 2012 2:41:32 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.events;

import java.util.List;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Turnable;
import net.wombatrpgs.mgne.core.lua.Lua;
import net.wombatrpgs.mgne.core.lua.LuaConvertable;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.MapMovable;
import net.wombatrpgs.mgne.scenes.SceneParser;
import net.wombatrpgs.mgne.scenes.StringSceneParser;
import net.wombatrpgs.mgneschema.graphics.DirMDO;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.DirVector;
import net.wombatrpgs.mgneschema.maps.data.DisplayType;
import net.wombatrpgs.mgneschema.maps.data.EightDir;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * A map event is any map object defined in Tiled, including characters and
 * teleports and other fun stuff. Revised as of 2012-01-30 to be anything that
 * exists on a Tiled layer, even if it wasn't created in Tiled itself.
 * 
 * MR: MapEvent is anything that exists in the world of tiles, as opposed to a
 * thing that just lives on a map. It isn't necessarily a character.
 * 
 * As of 2014-01-28, it's getting combined with character events to represent
 * any object on the map, whether it moves or not.
 */
public class MapEvent extends MapMovable implements	LuaConvertable, Turnable {
	
	/** General children and info */
	protected EventMDO mdo;
	protected FacesAnimation appearance;
	protected boolean eventHidden;
	protected boolean switchHidden;
	
	/** Lua */
	protected SceneParser onAdd, onRemove, onInteract, onCollide, onEnter;
	protected LuaValue turn;
	protected LuaValue lua;
	protected LuaValue hide;
	
	/** Tile-based positioning */
	protected int tileX, tileY;

	/**
	 * Creates a new map event for the level at the origin. It will need its
	 * location set. This is private so use one of the public constructors that
	 * takes location info instead.
	 * @param	mdo				The data to construct from
	 */
	protected MapEvent(EventMDO mdo) {
		super();
		this.mdo = mdo;
		
		zeroCoords();
		regenerateLua();
		
		if (mdoHasProperty(mdo.appearance)) {
			String mdoName;
			if (mdo.appearance.startsWith("lua")) {
				String script = mdo.appearance.substring(4, mdo.appearance.length() - 1);
				mdoName = MGlobal.lua.run(script, this).checkjstring();
			} else {
				mdoName = mdo.appearance;
			}
			DirMDO dirMDO = MGlobal.data.getEntryFor(mdoName, DirMDO.class);
			appearance = FacesAnimationFactory.create(dirMDO);
			appearance.startMoving();
			assets.add(appearance);
		}
		
		onAdd = mdoToScene(mdo.onAdd);
		onRemove = mdoToScene(mdo.onRemove);
		onInteract = mdoToScene(mdo.onInteract);
		onCollide = mdoToScene(mdo.onCollide);
		onEnter = mdoToScene(mdo.onEnter);
		
		if (mdo.face != null) {
			setFacing(mdo.face);
		} else {
			setFacing(OrthoDir.SOUTH);
		}
		eventHidden = mdo.hidden == DisplayType.HIDDEN;
	}
	
	/** Kryo constructor */
	protected MapEvent() { }
	
	/** @param tileX The new x-coord of this event (in tiles) */
	public void setTileX(int tileX) { this.tileX = tileX; }
	
	/** @param tileY The new y-coord of this event (in tiles) */
	public void setTileY(int tileY) { this.tileY = tileY; }
	
	/** @return x-coord of this object, in tiles */
	public int getTileX() { return tileX; }
	
	/** @return y-coord of this object, in tiles */
	public int getTileY() { return tileY; }
	
	/** @return The current appearance of this character */
	public FacesAnimation getAppearance() { return appearance; }
	
	/** @param appearance The new anim for this event */
	public void setAppearance(FacesAnimation appearance) { this.appearance = appearance; }
	
	/** @return True if the object is passable, false otherwise */
	public boolean isPassable() { return appearance == null; }
	
	/** @see net.wombatrpgs.mgne.core.lua.LuaConvertable#toLua() */
	@Override public LuaValue toLua() { return lua; }
	
	/** Hides this event so that it no longer renders or collides */
	public void hide() { eventHidden = true; }
	
	/** Shows this event so that it renders and collides */
	public void show() { eventHidden = false; }
	
	/**
	 * Gets the facing for the character. Returns null if no appearance.
	 * @return					The direction currently facing or null
	 */
	public OrthoDir getFacing() {
		if (appearance != null) {
			return appearance.getFacing();
		} else {
			return null;
		}
	}
	
	/**
	 * Tells the animation to face a specific direction if it exists.
	 * @param 	dir				The direction to face
	 */
	public void setFacing(OrthoDir dir) {
		if (appearance != null) {
			appearance.setFacing(dir);
		}
	}
	
	/**
	 * Gets the ID name of this event. If specified in Tiled, it should return
	 * that. If specified in database, it should return that. Basically,
	 * override this, otherwise it returns something really unhelpful.
	 * @return					The non-unique identifying name of this object
	 */
	public String getName() {
		return mdo.name;
	}
	
	/**
	 * Checks if this event's in a specific group. Events can belong to multiple
	 * groups if their group name contains the separator character. Events that
	 * are spawned from Tiled should probably override this.
	 * @param 	groupName		The name of the group we may be in
	 * @return					True if in that group, false if not
	 */
	public boolean inGroup(String groupName) {
		for (String group : mdo.groups.split(" ")) {
			if (groupName.equals(group)) return true;
		}
		return false;
	}
	
	/**
	 * Update yoself! This is called from the rendering loop but it's with some
	 * filters set on it for target framerate. As of 2012-01-30 it's not called
	 * from the idiotic update loop.
	 * @param 	elapsed			Time elapsed since last update, in seconds
	 */
	public void update(float elapsed) {
		super.update(elapsed);
		if (appearance != null) {
			appearance.update(elapsed);
		}
		if (hide != null) {
			switchHidden = MGlobal.lua.run(hide, lua).checkboolean();
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		regenerateLua();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		if (!eventHidden && !switchHidden) {
			super.render(batch);
			if (appearance != null) {
				renderLocal(batch, appearance, 0, 0);
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		if (turn != null) {
			MGlobal.lua.run(turn, lua);
		}
	}

	/**
	 * Determines the orthographic direction to some tile location.
	 * @param	tileX			The x-loc to get direction towards (in tiles)
	 * @param	tileY			The y-loc to get direction towards (in tiles)
	 * @return					
	 */
	public OrthoDir directionToTile(int tileX, int tileY) {
		return directionTo(tileX * parent.getTileWidth(), tileY * parent.getTileHeight()).toOrtho();
	}
	
	/**
	 * Calculates the manhattan distance between this and some other location.
	 * @param	tileX			The x-coord to calc distance to, in tiles
	 * @param	tileY			The y-coord to calc distance to, in tiles
	 * @return
	 */
	public int tileDistanceTo(int tileX, int tileY) {
		return Math.abs(tileX - this.tileX) + Math.abs(tileY - this.tileY); 
	}
	
	/**
	 * Calculates the euclidean distance between this and some other tile loc.
	 * @param	tileX			The other's tile x-coord
	 * @param	tileY			The other's tile y-coord
	 * @return					The distance to that event in tiles
	 */
	public float euclideanTileDistanceTo(int tileX, int tileY) {
		float dx = this.tileX - tileX;
		float dy = this.tileY - tileY;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Calculates the euclidean distance between this and some other event.
	 * @param	event			The event to get dist to
	 * @return					The distance to that event in tiles
	 */
	public float euclideanTileDistanceTo(MapEvent event) {
		return euclideanTileDistanceTo(event.getTileX(), event.getTileY());
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapMovable#onAddedToMap
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		runScene(onAdd);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onRemovedFromMap
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onRemovedFromMap(Level map) {
		super.onRemovedFromMap(map);
		runScene(onRemove);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusGained
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusGained(Level map) {
		super.onMapFocusGained(map);
		if (onEnter != null) {
			MGlobal.levelManager.getTele().getPost().addListener(new FinishListener() {
				@Override public void onFinish() {
					runScene(onEnter);
				}
			});
		}
	}

	/**
	 * What happens when a character moves into this event? By default, nothing
	 * happens, but characters should be attacked, items should be auto-grabbed,
	 * and so on. This will start evaluation when the hero stops moving.
	 * @param	event			The jerk that ran into us
	 */
	public void onCollide(MapEvent event) {
		if (event == MGlobal.getHero()) {
			event.addTrackingListener(new FinishListener() {
				@Override public void onFinish() {
					runScene(onCollide);
				}
			});
		}
	}
	
	/**
	 * The hero pressed A on you, now do something! For passable events, this
	 * happens when the hero is standing on the event, and for impassable ones,
	 * when the hero is facing it. Events that are non-interactive should return
	 * false.
	 * @return					True if an interaction happened, false if none
	 */
	public boolean onInteract() {
		if (mdoHasProperty(mdo.onInteract)) {
			faceToward(MGlobal.getHero());
			runScene(onInteract);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Makes this event face towards an object on the map.
	 * @param 	event			The object to face
	 */
	public void faceToward(MapEvent event) {
		faceToward(event.getTileX(), event.getTileY());
	}
	
	/**
	 * Makes this event face towards a tile location on the map.
	 * @param	tileX			The x-coord of the tile to face (in tiles)
	 * @param	tileY			The y-coord of the tile to face (in tiles)
	 */
	public void faceToward(int tileX, int tileY) {
		setFacing(directionToTile(tileX, tileY));
	}
	
	/**
	 * Face away from a particular map event.
	 * @param	event			The object to face away from
	 */
	public void faceAway(MapEvent event) {
		setFacing(EightDir.getOpposite(directionTo(event)).toOrtho(getFacing()));
	}
	
	/**
	 * Flashes our appearance a certain color for a certain time.
	 * @param	c				The color to flash
	 * @param	duration		How long the flash should take in total
	 */
	public void flash(Color c, float duration) {
		appearance.flash(c, duration);
	}
	
	/**
	 * Faces this event towards its current tile target.
	 */
	public void faceTarget() {
		float dx = tileX*parent.getTileWidth() - x;
		float dy = tileY*parent.getTileHeight() - y;
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				setFacing(OrthoDir.EAST);
			} else if (dx < 0) {
				setFacing(OrthoDir.WEST);
			}
		} else {
			if (dy < 0) {
				setFacing(OrthoDir.SOUTH);
			} else if (dy > 0) {
				setFacing(OrthoDir.NORTH);
			}
		}
	}
	
	/**
	 * Attempts to move to a specified tile location next to this character. If
	 * there's nothing there, the character will step to that location. If
	 * there his something there, this will hit it. Also adds an appropriate
	 * travel step.
	 * @param	targetX			The target location x-coord, in tiles
	 * @param	targetY			The target location y-coord, in tiles
	 * @return					True if the move succeeded, false if we hit
	 */
	public boolean attemptStep(int targetX, int targetY) {
		faceToward(targetX, targetY);
		if (parent.isTilePassable(targetX, targetY)) {
			List<MapEvent> events = parent.getEventsAt(targetX, targetY);
			boolean colliding = false;
			for (MapEvent event : events) {
				event.onCollide(this);
				if (!event.isPassable()) {
					colliding = true;
				}
			}
			if (!colliding) {
				step(directionToTile(targetX, targetY));
				tileX = targetX;
				tileY = targetY;
				return true;
			}
		} else {
			List<MapEvent> events = parent.getEventsAt(getTileX(), getTileY());
			for (MapEvent event : events) {
				if (event != this) {
					event.onCollide(this);
				}
			}
		}
		return false;
	}
	
	/**
	 * Attempts to step in a particular direction.
	 * @param	dir				The direction to step
	 * @return					True if the move succeeded, false if we hit
	 */
	public boolean attemptStep(OrthoDir dir) {
		int targetX = (int) (tileX + dir.getVector().x);
		int targetY = (int) (tileY + dir.getVector().y);
		return attemptStep(targetX, targetY);
	}
	
	/**
	 * Sets this event's location both in tile and pixels. This results in the
	 * event having a correct tile position and also rendering at that location.
	 * @param	tileX			The location to set at (in tiles)
	 * @param	tileY			The location to set at (in tiles)
	 */
	public void setTileLocation(int tileX, int tileY) {
		setTileX(tileX);
		setTileY(tileY);
		setX(tileX * parent.getTileWidth());
		setY(tileY * parent.getTileHeight());
	}
	
	/**
	 * Determines if this event will cause something to happen when the hero
	 * steps inside it. This is to make sure NPCs don't wander into teleports.
	 * @return					True if has a collision trigger
	 */
	public boolean hasCollideTrigger() {
		return onCollide != null && isPassable();
	}
	
	/**
	 * Runs a scene, probably one we generated from MDO.
	 * @param	chunk			The chunk of text to run, possibly null
	 */
	protected void runScene(SceneParser scene) {
		// maybe some safety checks should go here?
		if (scene != null) {
			scene.run();
		}
	}
	
	/**
	 * Reconstructs the lua value if none is currently available. This is called
	 * in the constructor and also post processing because the lua will be lost
	 * when this object is serialized.
	 */
	protected void regenerateLua() {
		if (lua == null) {
			lua = LuaValue.tableOf();
			Lua.generateFunction(this, lua, "getX");
			Lua.generateFunction(this, lua, "getY");
			Lua.generateFunction(this, lua, "getTileX");
			Lua.generateFunction(this, lua, "getTileY");
			Lua.generateFunction(this, lua, "isTracking");
			Lua.generateFunction(this, lua, "show");
			Lua.generateFunction(this, lua, "hide");
			lua.set("eventStep", new OneArgFunction() {
				@Override public LuaValue call(LuaValue dirArg) {
					String argString = dirArg.checkjstring();
					boolean result = attemptStep(OrthoDir.valueOf(argString));
					return CoerceJavaToLua.coerce(result);
				}
			});
			lua.set("eventFace", new OneArgFunction() {
				@Override public LuaValue call(LuaValue dirArg) {
					String argString = (String) CoerceLuaToJava.coerce(dirArg, String.class);
					setFacing(OrthoDir.valueOf(argString));
					return LuaValue.NIL;
				}
			});
			lua.set("eventWalk", new TwoArgFunction() {
				@Override public LuaValue call(LuaValue stepsArg, LuaValue dirArg) {
					OrthoDir dir = OrthoDir.valueOf(dirArg.checkjstring());
					int steps = stepsArg.checkint();
					int targetX = (int) (getTileX() + steps * dir.getVector().x);
					int targetY = (int) (getTileY() + steps * dir.getVector().y);
					step(dir);
					setFacing(dir);
					targetTile(targetX, targetY);
					tileX = targetX;
					tileY = targetY;
					return LuaValue.NIL;
				}
			});
			lua.set("wander", new ZeroArgFunction() {
				@Override public LuaValue call() {
					if (MGlobal.rand.nextInt(4) == 0) {
						for (int attempt = 0; attempt < 10; attempt += 1) {
							int index = MGlobal.rand.nextInt(OrthoDir.values().length);
							OrthoDir dir = OrthoDir.values()[index];
							int toX = (int) (getTileX() + dir.getVector().x);
							int toY = (int) (getTileY() + dir.getVector().y);
							if (!parent.isTilePassable(toX, toY)) {
								continue;
							}
							for (MapEvent event : parent.getEventsAt(toX, toY)) {
								if (event.hasCollideTrigger()) {
									continue;
								}
							}
							attemptStep(dir);
							break;
						}
					}
					return LuaValue.NIL;
				}
				
			});
			
			if (mdo != null && mdo.hide != null && mdo.hide.length() > 0) {
				hide = MGlobal.lua.interpret(mdo.hide);
			}
			if (mdo != null && mdo.onTurn != null && mdo.onTurn.length() > 0) {
				turn = MGlobal.lua.interpret(mdo.onTurn);
			}
		}
	}
	
	/**
	 * Moves a certain direction, regardless of passability, events, etc. That's
	 * why it's protected, don't use it directly.
	 * @param	dir				The direction to move
	 */
	protected void step(OrthoDir dir) {
		DirVector vec = dir.getVector();
		targetTile(
				(int) (tileX + vec.x),
				(int) (tileY + vec.y));
		setVelocity(
				vec.x * (parent.getTileWidth() / MGlobal.constants.getDelay()),
				vec.y * (parent.getTileHeight() / MGlobal.constants.getDelay()));
	}
	
	/**
	 * Converts and stores some Lua value. Constructor helper function.
	 * @param	lua				The lua to convert, could be null or empty
	 * @return					The scene parser from the script, or null
	 */
	protected SceneParser mdoToScene(String lua) {
		if (mdoHasProperty(lua) && lua.length() > 0) {
			SceneParser scene = new StringSceneParser(lua, toLua());
			assets.add(scene);
			return scene;
		} else {
			return null;
		}
	}

}
