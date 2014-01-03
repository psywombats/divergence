/**
 *  AnimationListener.java
 *  Created on Jan 30, 2013 11:56:43 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

/**
 * Listens for the end of an animation.
 */
public interface AnimationListener {
	
	/**
	 * Called when an animation has finished playing.
	 * @param 	source			The animation player that finished playing
	 */
	public void onAnimationFinish(AnimationPlayer source);

}
