/**
 *  AnimationPlayer.java
 *  Created on Jan 30, 2013 9:41:41 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.AnimationStrip;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;

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
		assets.add(animation);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		animation.render(camera);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		animation.update(elapsed);
		if (animation.isFinished() && animation.runsOnlyOnce()) {
			for (AnimationListener listener : listeners) {
				listener.onAnimationFinish(this);
			}
			parent.removeEvent(this);
			animation.stopMoving();
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		return animation.getRegion();
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
			MGlobal.reporter.warn("Unregistered a non-listener: " + listener);
		}
	}

}
