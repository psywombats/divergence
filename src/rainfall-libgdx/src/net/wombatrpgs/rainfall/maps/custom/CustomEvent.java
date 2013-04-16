/**
 *  GameObject.java
 *  Created on Mar 2, 2013 5:13:37 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom;

import com.badlogic.gdx.maps.MapObject;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.maps.CustomEventMDO;

/**
 * An event that resides on a map that's just kind of... miscellaneous. All of
 * this thing's subclasses are probably wildly different, so I guess this is
 * here for some sort of convenience. Actually as of 2013-03-02 this acts as a
 * storage point for events so that children can cross-query each other and
 * actually auto-register when they enter the level.
 */
public abstract class CustomEvent extends CharacterEvent {
	
	public static final String EVENT_PREFIX = "event_";
	
	protected static final String PROPERTY_ID = "id";
	
	protected CustomEventMDO mdo;
	protected String id; // this is taken from the tiled object

	/**
	 * Constructs a new game object from its tiled object. Unlike our barbarian
	 * parents, takes care of setting x/y based on the map's tile width and
	 * height. Because we're nice like that.
	 * @param mdo			Character data, look up stub entry in the database
	 * @param object		The tiled object itself
	 * @param parent		The parent map this object is from
	 */
	public CustomEvent(CustomEventMDO mdo, MapObject object, Level parent) {
		super(mdo, object, parent);
		this.mdo = mdo;
		if (getProperty(PROPERTY_ID) != null) {
			this.id = EVENT_PREFIX + getProperty(PROPERTY_ID);
		} else {
			RGlobal.reporter.warn("No event id on a custom object: " + this);
			this.id = "";
		}
		parent.registerCustomObject(this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#getSortY()
	 */
	@Override
	public float getSortY() {
		switch (mdo.sort) {
		case SAME_LEVEL_AS_HERO: return super.getSortY();
		case BELOW_HERO: return super.getSortY() + parent.getTileHeight();
		case ABOVE_HERO: return super.getSortY() - parent.getTileHeight();
		}
		RGlobal.reporter.warn("Some weird shit happened in above/below");
		return 0;
	}

	/**
	 * The ID string from the tiled object. Determines this objects features via
	 * hardcoded stuff.
	 * @return					This object's unique-ish ID
	 */
	public String getID() {
		return id;
	}

}
