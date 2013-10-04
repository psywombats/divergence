/**
 *  TimerListener.java
 *  Created on Jan 30, 2013 2:43:27 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.objects;

/**
 * Something that waits for a timer object to finish counting down and then
 * responds to it.
 */
public interface TimerListener {
	
	/**
	 * Called when a timer reaches zero.
	 * @param 	source			The timer that fired the event
	 */
	public void onTimerZero(TimerObject source);

}
