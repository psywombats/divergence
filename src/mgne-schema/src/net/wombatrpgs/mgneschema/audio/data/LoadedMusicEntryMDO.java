/**
 *  LoadedMusicEntryMDO.java
 *  Created on Oct 12, 2014 1:14:38 PM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.audio.data;

import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * Entry in the sound manager for loaded music.
 */
public class LoadedMusicEntryMDO extends HeadlessSchema {
	
	@Desc("Reference key - can be used to refer to this key in-game")
	public String refKey;
	
	@Desc("File - .mp3, .ogg etc containing the relevant music")
	@FileLink("audio")
	public String path;

}
