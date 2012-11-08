/**
 *  LoadingBarTask.java
 *  Created on Nov 8, 2012 1:44:53 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

/**
 * Any thing that can be done during a loading bar progress tick. This is only
 * meant to be run once.
 */
public abstract class LoadingBarRunnable {
	
	/**
	 * Do the loading you were assigned to do!
	 */
	public abstract void run();
	
	/**
	 * Evaluates how long in "units" this runnable should take. One ~4kb file
	 * load should be about 1 unit.
	 * @return		The length in time units of this runnable
	 */
	public abstract int getSize();

}
