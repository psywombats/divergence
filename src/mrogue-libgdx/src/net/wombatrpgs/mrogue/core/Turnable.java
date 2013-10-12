/**
 *  Turnable.java
 *  Created on Oct 12, 2013 4:55:24 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

/**
 * Something that can update based on a passed turn. Turns are counted in
 * actions the hero takes.
 */
public interface Turnable {
	
	/** 
	 * Called every time the hero takes a turn on a map shared by this thing.
	 */
	public void onTurn();

}
