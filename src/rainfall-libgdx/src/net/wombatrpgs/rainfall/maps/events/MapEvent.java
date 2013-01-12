/**
 *  MapEvent.java
 *  Created on Dec 24, 2012 2:41:32 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.characters.Hero;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.NoHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * A map event is any map object defined in Tiled, including characters and
 * teleports and other fun stuff.
 */
public abstract class MapEvent extends MapObject {
	
	/* these event types are defined in objecttypes.xml */
	protected static String EVENT_TYPE = "event";
	protected static String TELEPORT_TYPE = "teleport";
	protected static String Z_TELEPORT_TYPE = "z-teleport";
	
	/**
	 * Creates a new map event for the level at the specified coords.
	 * @param 	parent		The parent level of the event
	 * @param 	x			The x-coord to start at (in pixels)
	 * @param 	y			The y-coord to start at (in pixels);
	 */
	public MapEvent(Level parent, float x, float y) {
		super(parent, x, y);
	}

	/**
	 * Creates a new map event for the level at the origin.
	 * @param 	parent		The parent level of the event
	 */
	protected MapEvent(Level parent) {
		super(parent);
	}
	
	/**
	 * Creates a blank map event associated with no map. Assumes the subclass
	 * will do something interesting in its constructor.
	 */
	protected MapEvent() {
		// tsilb
	}
	
	/**
	 * Creates a new event for the supplied parent level using coordinates
	 * inferred from the tiled object.
	 * @param 	parent		The parent levelt to make teleport for
	 * @param 	object		The object to infer coords from
	 */
	protected MapEvent(Level parent, TiledObject object) {
		this(	parent, 
				object.x, 
				parent.getMap().height*parent.getMap().tileHeight-object.y);
	}

	/**
	 * Subclasses should override this, else they'll get the null hitbox.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return NoHitbox.getInstance();
	}
	
	
	/**
	 * Default is invisible.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) { }

	/**
	 * Default is nothing.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) { }

	/**
	 * Default is nothing.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) { }

	/**
	 * Default is nothing happens.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapObject, net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void onCollide(MapObject other, CollisionResult result) { }

	/**
	 * Overlapping is enabled by default.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#isOverlappingAllowed()
	 */
	@Override
	public boolean isOverlappingAllowed() {
		return true;
	}
	
	/**
	 * Handles an entry from the map and turns it into the relevant event(s).
	 * @param 	parent			The parent level to add events to
	 * @param 	object			The tiled object to create events from
	 * @param 	layerIndex		The index of the layer of the source event
	 */
	public static void handleData(Level parent, TiledObject object, int layerIndex) {
		if (EVENT_TYPE.equals(object.type) ||
			TELEPORT_TYPE.equals(object.type)) {
			parent.addEvent(createEvent(parent, object, layerIndex), layerIndex);
		} else if (Z_TELEPORT_TYPE.equals(object.type)) {
			parent.addEvent(createEvent(parent, object, layerIndex), layerIndex);
			parent.addEvent(createEvent(parent, object, layerIndex), layerIndex+1);
		} else {
			Global.reporter.warn("Was an event with no type: " + object.name);
		}
	}
	
	/**
	 * Creates a map object from tiled object data. Returns the appropriate
	 * subclass of MapEvent. Does not add the map event and is meant to be
	 * called internally.
	 * though.
	 * @param	parent			The level to craft the object for
	 * @param 	object			The tiled object data
	 * @param	layerIndex		The layer index that this object is intended for
	 * @return					Newly minted map object
	 */
	protected static MapEvent createEvent(Level parent, TiledObject object, int layerIndex) {
		if (EVENT_TYPE.equals(object.type)) {
			TiledMap map = parent.getMap();
			String mdoName = object.properties.get("key");
			CharacterEventMDO eventMdo = (CharacterEventMDO) RGlobal.data.getEntryByKey(mdoName);
			if (eventMdo.key.equals("hero_event")) {
				Hero hero = new Hero(parent, eventMdo, object.x, 
						map.height*map.tileHeight-object.y);
				RGlobal.hero = hero;
				return hero;
			} else {
				return new CharacterEvent(parent, eventMdo, object.x, 
						map.height*map.tileHeight-object.y);
			}
		} else if (TELEPORT_TYPE.equals(object.type)) {
			return new TeleportEvent(parent, object);
		} else if (Z_TELEPORT_TYPE.equals(object.type)) {
			return new ZTeleportEvent(parent, object, layerIndex);
		} else {
			Global.reporter.warn("Found an event with no type: " + object.name);
			return null;
		}
	}

}
