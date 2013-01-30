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
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.FallResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.events.MapEvent;

/**
 * A renderable collection of map events, grouped into a layer in a level.
 */
public class EventLayer extends Layer implements Renderable {
	
	protected Level parent;
	protected boolean passable[][];
	protected List<MapEvent> objects;
	protected TiledObjectGroup group;
	protected float z;
	
	/**
	 * Creates a new object layer with a parent level and no objects.
	 * @param 	parent		The parent level of the layer
	 * @param	group		The underlying tiled object
	 */
	public EventLayer(Level parent, TiledObjectGroup group) {
		this.parent = parent;
		this.objects = new ArrayList<MapEvent>();
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
			Global.reporter.warn("Group with no z-value on " + parent);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		// TODO: this can be optimized
		Collections.sort(objects);
		parent.getBatch().begin();
		for (int i = 0; i < objects.size(); i++) {
			MapObject object = objects.get(i);
			object.render(camera);
		}
		parent.getBatch().end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (MapObject object : objects) {
			object.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		for (MapObject object : objects) {
			object.postProcessing(manager);
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
	 * (net.wombatrpgs.rainfall.collisions.Hitbox)
	 */
	@Override
	public FallResult dropObject(Hitbox box) {
		FallResult result = new FallResult();
		result.finished = false;
		for (MapEvent object : objects) {
			if (object.getHitbox().isColliding(box).isColliding) {
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
	 * Adds another map event to this layer.
	 * @param 	event			The map event to add
	 */
	public void add(MapEvent event) {
		if (event == null) {
			Global.reporter.warn("Added a null object to the map?");
		} else {
			objects.add(event);
			event.onAdd(this);
		}
	}
	
	/**
	 * Removes a map object from this layer.
	 * @param 	event		The map object to remove
	 */
	public void remove(MapEvent event) {
		objects.remove(event);
	}
	
	/**
	 * Checks if a map object exists on this layer.
	 * @param 	mapObject		The map object to check if exists
	 * @return					True if the object exists on this layer
	 */
	public boolean contains(MapObject mapObject) {
		return objects.contains(mapObject);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#getZ()
	 */
	@Override
	public float getZ() {
		return z;
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
		// TODO: optimize these loops
		//if (!event.isMobile()) return;
		for (int i = 0; i < objects.size(); i++) {
			MapEvent other = objects.get(i);
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
