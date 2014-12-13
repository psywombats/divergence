/**
 *  AvatarMemory.java
 *  Created on Dec 13, 2014 2:34:32 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.events;

import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * Serialized portion of the avatar.
 */
public class AvatarMemory {
	
	public int tileX, tileY;
	public OrthoDir dir;
	public String animKey;
	
	/**
	 * Creates a serializable snapshot of the provided avatar.
	 * @param	avatar			The avatar to save
	 */
	public AvatarMemory(Avatar avatar) {
		this.tileX = avatar.getTileX();
		this.tileY = avatar.getTileY();
		this.dir = avatar.getFacing();
		this.animKey = avatar.getAppearance().getKey();
	}

}
