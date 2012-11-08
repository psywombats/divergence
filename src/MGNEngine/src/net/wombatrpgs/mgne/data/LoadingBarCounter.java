/**
 *  LoadingBar.java
 *  Created on Nov 8, 2012 1:40:41 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A pretty dumb class for keeping track of a bunch of load operations. It..
 * doesn't actually display things, it just keeps track of percentages. It also
 * extends the runnable interface itself so it can do cool things like store
 * sub-bars. This is only meant to be run once.
 */
public class LoadingBarCounter extends LoadingBarRunnable {
	
	protected List<LoadingBarRunnable> tasks;
	protected int completed, totalSize;
	// watch out here, these things don't update if you add more?

	/**
	 * Creates a new empty loading counter.
	 */
	public LoadingBarCounter() {
		tasks = new ArrayList<LoadingBarRunnable>();
		completed = 0;
		totalSize = 0;
	}
	
	/**
	 * Adds a new task onto the loading bar queue.
	 * @param task		The task to add
	 */
	public void addTask(LoadingBarRunnable task) {
		tasks.add(task);
		totalSize += task.getSize();
	}
	
	/**
	 * Returns true if loading on this bar has finished, false otherwise.
	 * @return		True if loading is complete
	 */
	public boolean isComplete() {
		return completed >= totalSize;
	}
	
	/**
	 * Gets the number of time units that have completed so far. Keep in mind
	 * this tracker assumes it's only run once.
	 * @return		The number of time units consumed so far
	 */
	public int getCompleted() {
		return completed;
	}

	@Override
	public void run() {
		for (LoadingBarRunnable task : tasks) {
			task.run();
			completed += task.getSize();
		}
	}

	@Override
	public int getSize() {
		return totalSize;
	}
}
