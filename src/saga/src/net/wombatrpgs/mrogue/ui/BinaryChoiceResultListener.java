/**
 *  BinaryChoiceResultListener.java
 *  Created on Oct 25, 2013 11:09:45 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

/**
 * What a mouthful. Callback for a dialog box.
 */
public interface BinaryChoiceResultListener {

	/**
	 * Called when the user makes a decision.
	 * @param	result				What the user decided
	 */
	public void onDecision(BinaryChoice result);
	
}
