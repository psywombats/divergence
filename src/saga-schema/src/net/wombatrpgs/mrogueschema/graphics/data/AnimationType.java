/**
 *  AnimationMode.java
 *  Created on Jan 22, 2013 6:46:16 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.data;

import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;

/**
 * All animations have a mode - whether to repeat indefinitely, play, or uh
 * do nothing I guess?
 */
@ExcludeFromTree
public enum AnimationType {
	
	REPEAT,
	PLAY_ONCE,
	DO_NOTHING,

}
