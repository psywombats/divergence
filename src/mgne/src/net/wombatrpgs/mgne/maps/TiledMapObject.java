/**
 *  TiledMapObject.java
 *  Created on Jan 22, 2014 9:26:39 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.events.EventType;
import net.wombatrpgs.mgne.physics.RectHitbox;
import net.wombatrpgs.mgns.core.MainSchema;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;

/**
 * A MGN wrapper for the LibGDX MapObject. Immutable.
 */
public class TiledMapObject {
	
	/** Properties in for Tiled map objects */
	// these ones are hard-coded by libgdx (the bastards)
	public static final String PROPERTY_X = "x";
	public static final String PROPERTY_Y = "y";
	public static final String PROPERTY_WIDTH = "width";
	public static final String PROPERTY_HEIGHT = "height";
	
	// mgn-specific
	public static final String PROPERTY_MDO = "mdo";
	public static final String PROPERTY_Z = "z";
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_KEY = "key";
	
	protected final LoadedLevel parent;
	protected final MapObject object;
	
	/**
	 * Creates a new wrapper for the given Tiled map object.
	 * @param	parent			The level this object is associated with
	 * @param	object			The libgdx/tiled object to wrap
	 */
	public TiledMapObject(LoadedLevel parent, MapObject object) {
		this.parent = parent;
		this.object = object;
	}
	
	/** @return The parent level of this tiled map object */
	public LoadedLevel getLevel() { return parent; }
	
	/**	@param key The key of the property to fetch
	 *	@return The string value of the provided key */
	public String getString(String key) { return object.getProperties().get(key, String.class); }
	
	/**	@param key The key of the property to fetch
	 *	@return The integer value of the provided key */
	public int getInt(String key) { return Integer.valueOf(object.getProperties().get(key, String.class)); }
	
	/**	@param key The key of the property to fetch
	 *	@return The float value of the provided key */
	public float getFloat(String key) { return object.getProperties().get(key, Float.class); }
	
	/** @return The x-coord of this object on the map (in pixels) */
	public int getX() { return Math.round(getFloat(PROPERTY_X)); }
	
	/** @return The y-coord of this object on the map (in pixels) */
	public int getY() { return Math.round(getFloat(PROPERTY_Y)); }
	
	/** @return The x-coord of this object on the map (in tiles) */
	public int getTileX() { return (int) (getX() / parent.getTileWidth()); }
	
	/** @return The y-coord of this object on the map (in tiles) */
	public int getTileY() { return (int) (getY() / parent.getTileWidth()); }
	
	/**
	 * Checks if a property exists on this object, regardless of value.
	 * @param	key				The key of the property to check
	 * @return					True if that property exists
	 */
	public boolean propertyExists(String key) {
		return object.getProperties().get(key) != null;
	}
	
	/**
	 * Reads the map object's event type from its property list. Reports a
	 * problem if the event has no event type.
	 * @return					The type of the map object (from property)
	 */
	public EventType getType() {
		String value = getString(PROPERTY_TYPE);
		EventType result;
		if (value == null) {
			MGlobal.reporter.err("No event type for map object " + this + " on map " + parent);
			return null;
		}
		try {
			result = EventType.valueOf(value);
		} catch (IllegalArgumentException e) {
			MGlobal.reporter.err(value + " is not a possible event type for " + this, e);
			return null;
		}
		return result;
	}
	
	/**
	 * Interprets this object as a polygon. Likely to blow up if this object is
	 * not a polygon, but hey, the libgdx underpinning is really brittle so
	 * don't blame me.
	 * @return					The polygon representation of this object
	 */
	public Polygon getPolygon() {
		if (PolygonMapObject.class.isAssignableFrom(object.getClass())) {
			return ((PolygonMapObject) object).getPolygon();
		} else {
			MGlobal.reporter.err("Is not a polygon: " + object);
			return null;
		}
	}
	
	/**
	 * Added for bacon
	 * @return
	 */
	public RectHitbox getRectHitbox() {
		if (RectangleMapObject.class.isAssignableFrom(object.getClass())) {
			RectangleMapObject rect = (RectangleMapObject) object;
			RectHitbox box = new RectHitbox(null,
					rect.getRectangle().x - getX(),
					rect.getRectangle().y - getY(),
					(rect.getRectangle().x + rect.getRectangle().width) - getX(),
					(rect.getRectangle().y + rect.getRectangle().height) - getY());
			return box;
		} else {
			MGlobal.reporter.err("No hitbox rect");
			return null;
		}
	}
	
	/**
	 * Attempts to generate an MDO for this object as if it was of the type
	 * provided. This involves a lot of reflection; essentially what happens is
	 * it attempts to map all properties to similarly-named fields in the MDO.
	 * @param	clazz			The class of MDO to generate
	 * @return					An instance of that MDO, with fields filled in
	 */
	@SuppressWarnings("unchecked")
	public <T extends MainSchema> T generateMDO(Class<T> clazz) {
		
		T mdo;
		try {
			mdo = clazz.newInstance();
		} catch (Exception e) {
			MGlobal.reporter.err("reflect-instantiate failed, ughhhhh", e);
			return null;
		}
		mdo.key = parent.getKeyName() + "." + getX() + "." + getY();
		mdo.description = "Autogenerated by TiledMapObject.generateMDO";
		mdo.name = object.getName();
		
		// crappy instanceof to set up width/height properties
		if (RectangleMapObject.class.isAssignableFrom(object.getClass())) {
			RectangleMapObject rect = (RectangleMapObject) object;
			object.getProperties().put(PROPERTY_WIDTH,
					rect.getRectangle().width / parent.getTileWidth());
			object.getProperties().put(PROPERTY_HEIGHT,
					rect.getRectangle().height / parent.getTileHeight());
		}
		
		// let's assign all values found on the object here
		Iterator<String> keys = object.getProperties().getKeys();
		while (keys.hasNext()) {
			String key = keys.next();
			if (key.equals(PROPERTY_X)) continue;
			if (key.equals(PROPERTY_Y)) continue;
			if (key.equals(PROPERTY_TYPE)) continue;
			try {
				Field field = clazz.getField(key);
				try {
					Object value = object.getProperties().get(key, field.getType());
					if (Enum.class.isAssignableFrom(field.getType())) {
						@SuppressWarnings("rawtypes")
						Class<? extends Enum> enumClass = (Class<? extends Enum>) field.getType();
						field.set(mdo, Enum.valueOf(enumClass, value.toString()));
					} else if (Integer.class.isAssignableFrom(field.getType())) {
						field.set(mdo, Integer.valueOf(value.toString()));
					} else {
						field.set(mdo, value);
					}
				} catch (Exception e) {
					MGlobal.reporter.warn("Bad field value " +
							object.getProperties().get(key) +
							" for object " + object +
							" on map " + parent);
				}
			} catch (Exception e) {
				MGlobal.reporter.warn("Bad field " + key + " on Tiled object " +
						object + " on map " + parent);
			}
		}
		
		return mdo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (object.getName() == null) ? "anon event" : object.getName() + 
				"("+getX()+","+getY()+") " + parent.getKeyName();
	}

}
