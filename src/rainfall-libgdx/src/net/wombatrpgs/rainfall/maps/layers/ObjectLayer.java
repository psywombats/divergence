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

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapEvent;
import net.wombatrpgs.rainfall.maps.MapObject;

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
		for (MapObject object : objects) {
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
		for (MapObject other : objects) {
			if (other != event) {
				CollisionResult result = event.getHitbox().isColliding(other.getHitbox());
				if (result.isColliding) {
					event.onCollide(other, result);
				}
			}
		}
	}

}
