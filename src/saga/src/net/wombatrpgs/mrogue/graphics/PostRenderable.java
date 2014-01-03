/**
 *  PostRenderable.java
 *  Created on Oct 18, 2013 1:28:59 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;


/**
 * A renderable that is only called to the final screen buffer.
 */
public interface PostRenderable extends Renderable {
	
	/**
	 * Do all your weird distortion shit here.
	 */
	public void renderPost();

}
