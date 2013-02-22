/**
 *  ObjectLayer.java
 *  Created on Nov 29, 2012 3:46:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.layers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.FallResult;
import net.wombatrpgs.rainfall.physics.Hitbox;

/**
 * A renderable collection of map events, grouped into a layer in a level.
 */
public class EventLayer extends Layer {
	
	protected Level parent;
	protected boolean passable[][];
	protected List<MapEvent> events;
	protected TiledObjectGroup group;
	protected float z;
	
	/**
	 * Creates a new object layer with a parent level and no objects.
	 * @param 	parent			The parent level of the layer
	 * @param	group			The underlying tiled object
	 */
	public EventLayer(Level parent, TiledObjectGroup group) {
		this.parent = parent;
		this.events = new ArrayList<MapEvent>();
		this.group = group;
		this.passable = new boolean[parent.getHeight()][parent.getWidth()];
		for (int y = 0; y < parent.getHeight(); y++) {
			for (int x = 0; x < parent.getWidth(); x++) {
				passable[y][x] = false;
			}
		}
		if (group.properties.containsKey(PROPERTY_Z)) {
			z = Float.valueOf(group.properties.get(PROPERTY_Z));
		} else {
			RGlobal.reporter.warn("Group with no z-value on " + parent);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera, int)
	 */
	@Override
	public void render(OrthographicCamera camera, int z) {
		// TODO: this can be optimized, but it's not fucking likely
		Collections.sort(events);
		parent.getBatch().begin();
		for (MapEvent event : events) {
			if (!event.requiresChunking()) {
				// render them once
				if ((int) Math.floor(getZ()) == z) {
					event.render(camera);
				}
			} else {
				// let's chunk 'em
				TextureRegion sprite = event.getRegion();
				int startY = (int) (z - Math.floor(getZ())) * parent.getTileHeight();
				if (startY < 0 || startY >= sprite.getRegionHeight()) continue;
				int origY = sprite.getRegionY();
				int origHeight = sprite.getRegionHeight();
				int newY = origY - startY + (origHeight - parent.getTileHeight());
				if (newY < origY) newY = origY;
				sprite.setRegionY(newY);
				if (origHeight > 32) sprite.setRegionHeight(32);
				event.renderLocal(camera, sprite, 0, startY, 0);
				sprite.setRegionY(origY);
				sprite.setRegionHeight(origHeight);
			}
		}
		parent.getBatch().end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (MapObject object : events) {
			object.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (MapObject object : events) {
			object.postProcessing(manager, pass);
		}
	}
	
	/**
	 * Provides collision response service to an event on this layer.
	 * @param 	event			The event to push out of collision
	 */
	@Override
	public void applyPhysicalCorrections(MapEvent event) {
		// this is no longer support here, do your collisions somewhere else
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		return false;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#dropObject
	 * (net.wombatrpgs.rainfall.physics.Hitbox)
	 */
	@Override
	public FallResult dropObject(Hitbox box) {
		FallResult result = new FallResult();
		result.finished = false;
		for (MapEvent object : events) {
			if (!object.supportsBlockLanding() && 
					object.getHitbox().isColliding(box).isColliding) {
				result.finished = true;
				result.cleanLanding = false;
				result.collidingObject = object;
				result.z = (int) Math.floor(getZ());
				break;
			}
		}
		return result;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#getZ()
	 */
	@Override
	public float getZ() {
		return z;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#finalizePassability()
	 */
	@Override
	public void finalizePassability() {
		// we don't care
	}

	/**
	 * Adds another map event to this layer.
	 * @param 	event			The map event to add
	 */
	public void add(MapEvent event) {
		if (event == null) {
			RGlobal.reporter.warn("Added a null object to the map?");
		} else {
			events.add(event);
			event.onAdd(this);
		}
	}
	
	/**
	 * Removes a map object from this layer.
	 * @param 	event		The map object to remove
	 */
	public void remove(MapEvent event) {
		events.remove(event);
	}
	
	/**
	 * Checks if a map object exists on this layer.
	 * @param 	mapObject		The map object to check if exists
	 * @return					True if the object exists on this layer
	 */
	public boolean contains(MapObject mapObject) {
		return events.contains(mapObject);
	}
	
	/**
	 * Checks if an event on this layer has made a point on the map passable.
	 * @param 	tileX			The x-coord (in tiles) to check
	 * @param 	tileY			The y-coord (in tiles) to check
	 * @return					True if that tile is passable via events
	 */
	public boolean isSpecialPassable(int tileX, int tileY) {
		return passable[tileY][tileX];
	}
	
	/**
	 * Sets a tile on this layer as passable via special event.
	 * @param 	tileX			The x-coord (in tiles) to set
	 * @param 	tileY			The y-coord (in tiles) to set
	 */
	public void setPassable(int tileX, int tileY) {
		passable[tileY][tileX] = true;
	}
	
	/**
	 * Run collision detection for an individual event on this layer.
	 * @param	event			The event to run the checks for
	 */
	public void detectCollisions(MapEvent event) {
		// TODO: optimize these loops if it becomes an issue
		//if (!event.isMobile()) return;
		for (int i = 0; i < events.size(); i++) {
			MapEvent other = events.get(i);
			if (other != event) {
				CollisionResult result = event.getHitbox().isColliding(other.getHitbox());
				if (result.isColliding) {
					boolean res1 = event.onCollide(other, result);
					boolean res2 = other.onCollide(event, result);
					if (!event.isOverlappingAllowed() && 
						! other.isOverlappingAllowed() &&
						!res1 && !res2) {
						event.resolveCollision(other, result);
					}
					break;
				}
			}
		}
	}

}
