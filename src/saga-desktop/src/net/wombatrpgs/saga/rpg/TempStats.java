/**
 *  TempStats.java
 *  Created on Apr 25, 2014 2:52:10 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.mgne.core.MGlobal;

/**
 * Struct for a stat set associated with character.
 */
public class TempStats {
	
	protected Chara chara;
	protected SagaStats stats;
	protected boolean done;
	
	/**
	 * Creates a new temporary stat modifier for a character. This will apply
	 * the stats.
	 * @param	chara			The character to modifer
	 * @param	stats			The stats to modify by
	 */
	public TempStats(Chara chara, SagaStats stats) {
		this.chara = chara;
		this.stats = stats;
		chara.applyStatset(stats, false);
		done = false;
	}
	
	/**
	 * Removes the statset from the character.
	 */
	public void decombine() {
		if (!done) {
			chara.applyStatset(stats, true);
		} else {
			MGlobal.reporter.warn("Tried double deapply to " + chara);
		}
	}

}
