/**
 *  FocusReporter.java
 *  Created on Nov 26, 2012 2:25:46 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;

/**
 * Reports to listeners if focus is gained or lost. Must be implemented in a
 * platform-specific manner.
 */
public abstract class FocusReporter {
	
	private List<FocusListener> listeners;
	
	/**
	 * Initializes a new reporter.
	 */
	public FocusReporter() {
		this.listeners = new ArrayList<FocusListener>();
	}
	
	/**
	 * Adds a new listener to this reporter.
	 * @param 	listener			The listener to receive focus events
	 */
	public final void registerListener(FocusListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener from this reporter.
	 * @param 	listener			The listener to no longer receive events
	 */
	public final void removeListener(FocusListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			MGlobal.reporter.warn("No listener found in list: " + listener);
		}
	}
	
	/**
	 * Called by the listeners when they render.
	 */
	public abstract void update();

	/**
	 * Tell the listeners we gained focus.
	 */
	protected final void reportFocusGained() {
		for (FocusListener listener : listeners) {
			listener.onFocusGained();
		}
	}
	
	/**
	 * Tell the listeners we lost focus.
	 */
	protected final void reportFocusLost() {
		for (FocusListener listener : listeners) {
			listener.onFocusLost();
		}
	}
}
