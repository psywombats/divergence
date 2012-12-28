/**
 *  ObjectLayer.java
 *  Created on Nov 29, 2012 3:46:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.layers;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.events.MapEvent;

/**
 * A renderable collection of map objects, grouped into a layer in a level.
 */
public class ObjectLayer extends Layer implements Renderable {
	
	protected Level parent;
	protected List<MapObject> objects;
	
	/**
	 * Creates a new object layer with a parent level and group of objects.
	 * @param 	parent		The parent level of the layer
	 * @param 	objects		The object that comprise the group
	 */
	public ObjectLayer(Level parent, List<MapObject> objects) {
		this.parent = parent;
		this.objects = objects;
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
		for (int i = 0; i < objects.size(); i++) {
			MapObject other = objects.get(i);
			if (other != event) {
				CollisionResult result = event.getHitbox().isColliding(other.getHitbox());
				if (result.isColliding) {
					event.onCollide(other, result);
					other.onCollide(event, result);
					break;
				}
			}
		}
	}
	
	/**
	 * Adds another map object to this layer.
	 * @param 	mapObject		The map object to add
	 */
	public void add(MapObject mapObject) {
		if (mapObject == null) {
			Global.reporter.warn("Added a null object to the map?");
		} else {
			objects.add(mapObject);
		}
	}
	
	/**
	 * Removes a map object from this layer.
	 * @param 	mapObject		The map object to remove
	 */
	public void remove(MapObject mapObject) {
		objects.remove(mapObject);
	}
	
	/**
	 * Checks if a map object exists on this layer.
	 * @param 	mapObject		The map object to check if exists
	 * @return					True if the object exists on this layer
	 */
	public boolean contains(MapObject mapObject) {
		return objects.contains(mapObject);
	}

}
