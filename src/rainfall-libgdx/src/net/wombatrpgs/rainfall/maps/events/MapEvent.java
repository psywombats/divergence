/**
 *  MapEvent.java
 *  Created on Dec 24, 2012 2:41:32 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.NoHitbox;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;

/**
 * A map event is any map object defined in Tiled, including characters and
 * teleports and other fun stuff.
 */
public abstract class MapEvent extends MapObject {
	
	protected boolean mobile;
	protected boolean checkCollisions;
	
	/**
	 * Creates a new map event for the level at the specified coords.
	 * @param 	parent			The parent level of the event
	 * @param 	x				The x-coord to start at (in pixels)
	 * @param 	y				The y-coord to start at (in pixels);
	 * @param	mobile			True if this object will be moving, else false
	 * @param	checkCollisions	True if collision detection should be enabled
	 */
	public MapEvent(Level parent, float x, float y, boolean mobile, boolean checkCollisions) {
		super(parent, x, y);
		this.mobile = mobile;
		this.checkCollisions = checkCollisions;
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
	protected MapEvent(Level parent, TiledObject object, boolean mobile, boolean checkCollisions) {
		this(	parent, 
				object.x, 
				parent.getMap().height*parent.getMap().tileHeight-object.y,
				mobile,
				checkCollisions);
	}

	/**
	 * Subclasses should override this, else they'll get the null hitbox.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return NoHitbox.getInstance();
	}
	
	/** @return True if this event moves, false otherwise */
	public boolean isMobile() { return mobile; }
	
	/** @return True if collisions should be checked on this event */
	public boolean isCollisionEnabled() { return checkCollisions; }
	
	/** @param enabled True if collisions should be checked on this event */
	public void setCollisionsEnabled(boolean enabled) { this.checkCollisions = enabled; }
	
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
	 * Default does nothing.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapObject, net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void onCollide(MapObject other, CollisionResult result) { 
		
	}

	/**
	 * Overlapping is enabled by default.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#isOverlappingAllowed()
	 */
	@Override
	public boolean isOverlappingAllowed() {
		return true;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onTeleOn(net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void onTeleOn(Level map) {
		super.onTeleOn(map);
		map.registerEvent(this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onTeleOff(net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void onTeleOff(Level map) {
		super.onTeleOff(map);
		map.unregisterEvent(this);
	}

	/**
	 * Moves objects out of collision with each other. Usually call this from
	 * onCollide, as a collision result is needed.
	 * @param 	other			The other object to bump
	 * @param 	result			The result of the two objects' collisions
	 */
	protected void applyMTV(MapObject other, CollisionResult result) {
		if (this.getHitbox() == result.collide2) {
			result.mtvX *= -1;
			result.mtvY *= -1;
		}
		this.x += result.mtvX;
		this.y += result.mtvY;
	}

}
