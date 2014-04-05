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
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
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
public class MapEvent extends MapMovable implements	LuaConvertable {
	
	/** General children and info */
	protected EventMDO mdo;
	protected FacesAnimation appearance;
	
	/** Lua */
	protected SceneParser onAdd, onRemove, onInteract, onCollide;
	protected LuaValue lua;
	
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
		
		if (mdoHasProperty(mdo.appearance)) {
			DirMDO dirMDO = MGlobal.data.getEntryFor(mdo.appearance, DirMDO.class);
			appearance = FacesAnimationFactory.create(dirMDO);
			appearance.startMoving();
			assets.add(appearance);
		}
		
		onAdd = mdoToScene(mdo.onAdd);
		onRemove = mdoToScene(mdo.onRemove);
		onInteract = mdoToScene(mdo.onInteract);
		onCollide = mdoToScene(mdo.onCollide);
		
		zeroCoords();
		regenerateLua();
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
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		regenerateLua();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		if (appearance != null) {
			appearance.render(camera);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#renderLocal
	 * (com.badlogic.gdx.graphics.OrthographicCamera,
	 * com.badlogic.gdx.graphics.g2d.TextureRegion, int, int, int)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite, 
			int offX, int offY, int angle) {
		super.renderLocal(camera,
				sprite,
				(int) (getX() + offX),
				(int) (getY() + offY), 
				angle);
	}
	
	/**
	 * Uses this event's x/y to render locally.
	 * @see net.wombatrpgs.mgne.maps.MapThing#renderLocal
	 * (com.badlogic.gdx.graphics.OrthographicCamera, 
	 * com.badlogic.gdx.graphics.g2d.TextureRegion, int, int)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite) {
		super.renderLocal(camera, sprite, (int) getX(), (int) getY(), 0);
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
					// travelPlan.add(new StepBump(this, directionTo(targetX, targetY)));
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
			// travelPlan.add(new StepBump(this, directionTo(targetX, targetY)));
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
			lua.set("attemptStep", new OneArgFunction() {
				@Override public LuaValue call(LuaValue arg) {
					String argString = (String) CoerceLuaToJava.coerce(arg, String.class);
					boolean result = attemptStep(OrthoDir.valueOf(argString));
					return CoerceJavaToLua.coerce(result);
				}
			});
			lua.set("face", new OneArgFunction() {
				@Override public LuaValue call(LuaValue arg) {
					String argString = (String) CoerceLuaToJava.coerce(arg, String.class);
					setFacing(OrthoDir.valueOf(argString));
					return LuaValue.NIL;
				}
			});
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
			SceneParser scene = new StringSceneParser(lua);
			assets.add(scene);
			return scene;
		} else {
			return null;
		}
	}

}
