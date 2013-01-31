/**
 *  AnimationPlayer.java
 *  Created on Jan 30, 2013 9:41:41 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.AnimationStrip;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;

/**
 * Plays an animation at a spot on the map. Does not do collisions, but can be
 * ordered to queue its resources.
 */
public class AnimationPlayer extends MapEvent {
	
	protected List<AnimationListener> listeners;
	protected AnimationStrip animation;
	
	/**
	 * Creates a new animation player. The animation does not start
	 * automatically nor is it associated with a particular map.
	 * @param	mdo				The data containing the animation to play
	 */
	public AnimationPlayer(AnimationMDO mdo) {
		super();
		this.animation = new AnimationStrip(mdo, this);
		this.listeners = new ArrayList<AnimationListener>();
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		animation.render(camera);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		animation.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		super.postProcessing(manager);
		animation.postProcessing(manager);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		animation.update(elapsed);
		if (animation.isFinished()) {
			for (AnimationListener listener : listeners) {
				listener.onAnimationFinish(this);
			}
			parent.removeEvent(this);
			animation.stopMoving();
		}
	}

	/**
	 * Starts the underlying animation, from the beginning, like normal. As the
	 * animation is delayed setup, requires a few extra parameters.
	 */
	public void start() {
		animation.reset();
		animation.startMoving();
	}
	
	/**
	 * Registers a listener to recieve notification when the animation is done
	 * playing.
	 * @param 	listener			The listener to notify when finished
	 */
	public void addListener(AnimationListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unregisters a listener so that it no longer recieves events.
	 * @param 	listener			The listener to remove
	 */
	public void removeListener(AnimationListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			RGlobal.reporter.warn("Unregistered a non-listener: " + listener);
		}
	}

}
