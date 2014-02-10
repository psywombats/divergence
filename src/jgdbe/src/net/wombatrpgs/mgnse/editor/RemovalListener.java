/**
 *  RemovalListener.java
 *  Created on Oct 12, 2012 1:03:37 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import javax.swing.JComponent;

/**
 * Interace to listen for when objects are removed from the editor.
 */
public interface RemovalListener {
	
	/**
	 * Called when the object is removed from the editor's array.
	 * @param contents		The object removed
	 */
	public void onRemoved(JComponent contents);

}
